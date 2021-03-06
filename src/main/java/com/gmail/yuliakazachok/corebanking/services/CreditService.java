package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.dto.CreditCreate;
import com.gmail.yuliakazachok.corebanking.dto.CreditInfo;
import com.gmail.yuliakazachok.corebanking.entities.*;
import com.gmail.yuliakazachok.corebanking.entities.states.PaymentScheduleStates;
import com.gmail.yuliakazachok.corebanking.repositories.*;
import com.gmail.yuliakazachok.corebanking.entities.states.ClientStates;
import com.gmail.yuliakazachok.corebanking.entities.states.CreditStates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final AvailableTariffRepository availableTariffRepository;
    private final TariffRepository tariffRepository;
    private final ClientRepository clientRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;

    public CreditInfo getActiveCreditByPassport(Long numberPassport) {
        List<AvailableTariff> listAvailableTariff = availableTariffRepository.findAllByNumberPassport(numberPassport);
        List<Integer> listIds = listAvailableTariff.stream().map(AvailableTariff::getId).collect(Collectors.toList());
        Credit credit = creditRepository.findCreditByStateAndIdAvailTariffIn(CreditStates.STATE_ACTIVE, listIds);
        AvailableTariff availableTariff = availableTariffRepository.findAvailableTariffById(credit.getIdAvailTariff());
        Tariff tariff = tariffRepository.findTariffById(availableTariff.getIdTariff());
        return new CreditInfo(credit.getId(), numberPassport, credit.getDateOpen(), tariff.getRate(), credit.getTerm(), credit.getSum(), credit.getState());
    }

    @Transactional
    public void saveCredit(CreditCreate creditCreate) {
        Tariff tariff = tariffRepository.findTariffById(creditCreate.getIdTariff());
        Integer sum = creditCreate.getSum();
        Integer term = creditCreate.getTerm();
        Long numberPassport = creditCreate.getNumberPassport();
        Date dateOpen = creditCreate.getDateOpen();
        if (sum >= tariff.getMinSum() && sum <= tariff.getMaxSum() && term >= tariff.getMinTerm() && term <= tariff.getMaxTerm()) {
            List<Integer> idsAvailTariffs = availableTariffRepository.findAllByNumberPassport(numberPassport)
                    .stream().map(AvailableTariff::getId).collect(Collectors.toList());
            List<Credit> listCredits = creditRepository.findAllByStateAndIdAvailTariffIn(CreditStates.STATE_ACTIVE, idsAvailTariffs);
            if (listCredits.isEmpty()) {
                Integer idAvailTariff = availableTariffRepository
                        .findByNumberPassportAndIdTariff(numberPassport, creditCreate.getIdTariff()).getId();
                Credit newCredit = creditRepository.save(new Credit(idAvailTariff, dateOpen, term, sum, CreditStates.STATE_ACTIVE));
                clientRepository.findById(numberPassport).ifPresent(client -> client.setState(ClientStates.STATE_YES_CREDIT));
                createPaymentSchedule(sum, term, tariff.getRate(), dateOpen, newCredit.getId());
            }
        } else {
            throw new IllegalArgumentException("???????????????? ??????????????????");
        }
    }

    private void createPaymentSchedule(Integer sum, Integer term, Integer rate, Date dateOpen, Integer idCredit) {
        float r = ((float) rate / 100 / 12);
        float sumMonth = (float) (sum * ((r * Math.pow(1 + r, term)) / (Math.pow(1 + r, term) - 1)));
        sumMonth = (float) Math.round(sumMonth * 100f) / 100f;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOpen);
        Date date = Date.valueOf(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        for (int i = 0; i < term; i++) {
            calendar.add(Calendar.MONTH, 1);
            date.setTime(calendar.getTime().getTime());
            paymentScheduleRepository.save(new PaymentSchedule(idCredit, date, sumMonth, PaymentScheduleStates.STATE_NOT_PAID));
        }
    }

    public List<CreditInfo> getHistoryCredit(Long number) {
        List<AvailableTariff> listAvailableTariff = availableTariffRepository.findAllByNumberPassport(number);
        List<Integer> listIds = listAvailableTariff.stream().map(AvailableTariff::getId).collect(Collectors.toList());
        List<Credit> listCredits = creditRepository.findAllByStateAndIdAvailTariffIn(CreditStates.STATE_CLOSE, listIds);
        List<CreditInfo> listCreditInfo = new ArrayList<>(Collections.emptyList());
        for (Credit credit: listCredits) {
            AvailableTariff availableTariff = availableTariffRepository.findAvailableTariffById(credit.getIdAvailTariff());
            Tariff tariff = tariffRepository.findTariffById(availableTariff.getIdTariff());
            listCreditInfo.add(new CreditInfo(
                    credit.getId(), availableTariff.getNumberPassport(), credit.getDateOpen(), tariff.getRate(), credit.getTerm(), credit.getSum(), credit.getState()
            ));
        }
        return listCreditInfo;
    }

    public CreditInfo getCreditById(Integer id) {
        Credit credit = creditRepository.findCreditById(id);
        AvailableTariff availableTariff = availableTariffRepository.findAvailableTariffById(credit.getIdAvailTariff());
        Tariff tariff = tariffRepository.findTariffById(availableTariff.getIdTariff());
        return new CreditInfo(credit.getId(), availableTariff.getNumberPassport(), credit.getDateOpen(), tariff.getRate(), credit.getTerm(), credit.getSum(), credit.getState());
    }
}
