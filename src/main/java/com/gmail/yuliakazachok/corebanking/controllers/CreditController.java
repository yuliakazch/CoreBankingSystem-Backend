package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.dto.CreditCreate;
import com.gmail.yuliakazachok.corebanking.dto.CreditInfo;
import com.gmail.yuliakazachok.corebanking.services.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/active/{number}")
    public CreditInfo getActiveCreditByPassport(@PathVariable Long number) {
        return creditService.getActiveCreditByPassport(number);
    }

    @PostMapping
    public void saveCredit(@RequestBody CreditCreate creditCreate) {
        creditService.saveCredit(creditCreate);
    }
}
