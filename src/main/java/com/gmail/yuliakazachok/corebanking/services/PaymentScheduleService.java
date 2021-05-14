package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.PaymentSchedule;
import com.gmail.yuliakazachok.corebanking.repositories.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    public List<PaymentSchedule> getPaymentScheduleByIdCredit(Integer idCredit) {
        return paymentScheduleRepository.findAllByIdCreditOrderByDate(idCredit);
    }
}
