package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.dto.PaymentInfo;
import com.gmail.yuliakazachok.corebanking.entities.Payment;
import com.gmail.yuliakazachok.corebanking.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public List<PaymentInfo> getPaymentsByIdCredit(@PathVariable Integer id) {
        return paymentService.getPaymentsByIdCredit(id);
    }

    @PostMapping
    public void savePayment(@RequestBody Payment payment) {
        paymentService.savePayment(payment);
    }
}
