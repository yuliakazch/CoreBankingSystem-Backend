package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.CreditInfo;
import com.gmail.yuliakazachok.corebanking.services.CreditService;
import com.gmail.yuliakazachok.corebanking.utils.paths.CreditPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CreditPaths.CONTROLLER)
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/active/{number}")
    public CreditInfo getActiveCreditByPassport(@PathVariable Long number) {
        return creditService.getActiveCreditByPassport(number);
    }
}
