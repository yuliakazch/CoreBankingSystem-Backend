package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.Сommission;
import com.gmail.yuliakazachok.corebanking.services.СommissionService;
import com.gmail.yuliakazachok.corebanking.utils.paths.CommissionPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CommissionPaths.CONTROLLER)
@RequiredArgsConstructor
public class СommissionController {

    private final СommissionService commissionService;

    @GetMapping
    public List<Сommission> getAll() {
        return commissionService.getAll();
    }

    @PostMapping
    public void save(@RequestBody Сommission commission) {
        commissionService.save(commission);
    }
}
