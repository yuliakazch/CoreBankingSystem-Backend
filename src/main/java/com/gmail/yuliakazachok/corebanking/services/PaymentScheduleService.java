package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.dto.PaymentScheduleInfo;
import com.gmail.yuliakazachok.corebanking.entities.PaymentSchedule;
import com.gmail.yuliakazachok.corebanking.repositories.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    public List<PaymentScheduleInfo> getPaymentScheduleByIdCredit(Integer idCredit) {
        List<PaymentSchedule> listSchedule = paymentScheduleRepository.findAllByIdCreditOrderByDate(idCredit);
        return listSchedule.stream().map(paymentSchedule ->
                new PaymentScheduleInfo(paymentSchedule.getDate(), paymentSchedule.getSum(), paymentSchedule.getState())
        ).collect(Collectors.toList());
    }
}
