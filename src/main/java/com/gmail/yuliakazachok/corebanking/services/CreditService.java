package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.*;
import com.gmail.yuliakazachok.corebanking.repositories.*;
import com.gmail.yuliakazachok.corebanking.utils.ClientStates;
import com.gmail.yuliakazachok.corebanking.utils.CreditStates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
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
        Credit credit = creditRepository.findCreditByStateAndIdAvailTariffIn(CreditStates.STATE_ACTIVE.ordinal(), listIds).get();
        AvailableTariff availableTariff = availableTariffRepository.findById(credit.getIdAvailTariff()).get();
        Tariff tariff = tariffRepository.findById(availableTariff.getIdTariff()).get();
        return new CreditInfo(credit.getId(), numberPassport, credit.getDateOpen(), tariff.getRate(), credit.getTerm(), credit.getSum(), credit.getState());
    }

    @Transactional
    public void saveCredit(CreditCreate creditCreate) {
        Tariff tariff = tariffRepository.findById(creditCreate.getIdTariff()).get();
        Integer sum = creditCreate.getSum();
        Integer term = creditCreate.getTerm();
        Long numberPassport = creditCreate.getNumberPassport();
        Date dateOpen = creditCreate.getDateOpen();
        if (sum >= tariff.getMinSum() && sum <= tariff.getMaxSum() && term >= tariff.getMinTerm() && term <= tariff.getMaxTerm()) {
            List<Integer> idsAvailTariffs = availableTariffRepository.findAllByNumberPassport(numberPassport)
                    .stream().map(AvailableTariff::getId).collect(Collectors.toList());
            List<Credit> listCredits = creditRepository.findAllByStateAndIdAvailTariffIn(CreditStates.STATE_ACTIVE.ordinal(), idsAvailTariffs);
            if (listCredits.isEmpty()) {
                Integer idAvailTariff = availableTariffRepository
                        .findByNumberPassportAndIdTariff(numberPassport, creditCreate.getIdTariff()).getId();
                Credit newCredit = creditRepository.save(new Credit(idAvailTariff, dateOpen, term, sum, CreditStates.STATE_ACTIVE.ordinal()));
                clientRepository.findById(numberPassport).ifPresent(client -> client.setState(ClientStates.STATE_YES_CREDIT.ordinal()));
                createPaymentSchedule(sum, term, tariff.getRate(), dateOpen, newCredit.getId());
            }
        }
    }

    private void createPaymentSchedule(Integer sum, Integer term, Integer rate, Date dateOpen, Integer idCredit) {
        float r = ((float) rate / 100 / 12);
        float sumMonth = (float) (sum * ((r * Math.pow(1 + r, term))/(Math.pow(1 + r, term) - 1)));
        sumMonth = (float) Math.round(sumMonth * 100) / 100;
        //double scale = Math.pow(10, 2);
        //sumMonth = (float) (Math.ceil(sumMonth * scale) / scale);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOpen);
        Date date = Date.valueOf(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        for (int i = 0; i < term; i++) {
            calendar.add(Calendar.MONTH, 1);
            date.setTime(calendar.getTime().getTime());
            paymentScheduleRepository.save(new PaymentSchedule(idCredit, date, sumMonth));
        }
    }
}
