package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.AvailableTariff;
import com.gmail.yuliakazachok.corebanking.entities.Credit;
import com.gmail.yuliakazachok.corebanking.entities.CreditInfo;
import com.gmail.yuliakazachok.corebanking.entities.Tariff;
import com.gmail.yuliakazachok.corebanking.repositories.AvailableTariffRepository;
import com.gmail.yuliakazachok.corebanking.repositories.CreditRepository;
import com.gmail.yuliakazachok.corebanking.repositories.TariffRepository;
import com.gmail.yuliakazachok.corebanking.utils.CreditStates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final AvailableTariffRepository availableTariffRepository;
    private final TariffRepository tariffRepository;

    public CreditInfo getActiveCreditByPassport(Long numberPassport) {
        List<AvailableTariff> listAvailableTariff = availableTariffRepository.findAllByNumberPassport(numberPassport);
        List<Integer> listIds = listAvailableTariff.stream().map(AvailableTariff::getId).collect(Collectors.toList());
        Credit credit = creditRepository.findCreditByStateAndIdAvailTariffIn(CreditStates.STATE_ACTIVE.ordinal(), listIds).get();
        AvailableTariff availableTariff = availableTariffRepository.findById(credit.getIdAvailTariff()).get();
        Tariff tariff = tariffRepository.findById(availableTariff.getIdTariff()).get();
        return new CreditInfo(credit.getId(), numberPassport, credit.getDateOpen(), tariff.getRate(), credit.getTerm(), credit.getSum(), credit.getState());
    }
}
