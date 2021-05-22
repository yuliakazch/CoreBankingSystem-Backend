package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.dto.CreditCreate;
import com.gmail.yuliakazachok.corebanking.dto.CreditInfo;
import com.gmail.yuliakazachok.corebanking.entities.Credit;
import com.gmail.yuliakazachok.corebanking.services.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/active/{number}")
    public CreditInfo getActiveCreditByPassport(@PathVariable Long number) {
        return creditService.getActiveCreditByPassport(number);
    }

    @GetMapping("/{id}")
    public CreditInfo getCreditById(@PathVariable Integer id) {
        return creditService.getCreditById(id);
    }

    @GetMapping("/history/{number}")
    public List<CreditInfo> getHistoryCredits(@PathVariable Long number) {
        return creditService.getHistoryCredit(number);
    }

    @PostMapping
    public void saveCredit(@RequestBody CreditCreate creditCreate) {
        creditService.saveCredit(creditCreate);
    }
}
