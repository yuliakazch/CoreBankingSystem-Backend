package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.*;
import com.gmail.yuliakazachok.corebanking.repositories.AvailableTariffRepository;
import com.gmail.yuliakazachok.corebanking.repositories.ClientRepository;
import com.gmail.yuliakazachok.corebanking.repositories.CreditRepository;
import com.gmail.yuliakazachok.corebanking.repositories.TariffRepository;
import com.gmail.yuliakazachok.corebanking.utils.ClientStates;
import com.gmail.yuliakazachok.corebanking.utils.CreditStates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final AvailableTariffRepository availableTariffRepository;
    private final TariffRepository tariffRepository;
    private final ClientRepository clientRepository;

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
        if (sum >= tariff.getMinSum() && sum <= tariff.getMaxSum() && term >= tariff.getMinTerm() && term <= tariff.getMaxTerm()) {
            List<Integer> idsAvailTariffs = availableTariffRepository.findAllByNumberPassport(numberPassport)
                    .stream().map(AvailableTariff::getId).collect(Collectors.toList());
            List<Credit> listCredits = creditRepository.findAllByStateAndIdAvailTariffIn(CreditStates.STATE_ACTIVE.ordinal(), idsAvailTariffs);
            if (listCredits.isEmpty()) {
                Integer idAvailTariff = availableTariffRepository
                        .findByNumberPassportAndIdTariff(numberPassport, creditCreate.getIdTariff()).getId();
                creditRepository.save(new Credit(
                        idAvailTariff,
                        creditCreate.getDateOpen(),
                        creditCreate.getTerm(),
                        creditCreate.getSum(),
                        CreditStates.STATE_ACTIVE.ordinal()
                ));
                clientRepository.findById(numberPassport).ifPresent(client -> client.setState(ClientStates.STATE_YES_CREDIT.ordinal()));
            }
        }
    }
}
