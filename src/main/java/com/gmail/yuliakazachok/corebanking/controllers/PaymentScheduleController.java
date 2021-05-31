package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.dto.PaymentScheduleInfo;
import com.gmail.yuliakazachok.corebanking.services.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment/schedule")
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final PaymentScheduleService paymentScheduleService;

    @GetMapping("/{id}")
    public List<PaymentScheduleInfo> getPaymentsScheduleByIdCredit(@PathVariable Integer id) {
        return paymentScheduleService.getPaymentScheduleByIdCredit(id);
    }
}
