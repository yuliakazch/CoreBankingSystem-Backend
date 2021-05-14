package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.Payment;
import com.gmail.yuliakazachok.corebanking.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> getPaymentsByIdCredit(Integer idCredit) {
        return paymentRepository.findAllByIdCreditOrderByDate(idCredit);
    }
}
