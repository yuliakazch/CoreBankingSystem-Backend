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

    @GetMapping("/{name}")
    public Сommission getByName(@PathVariable String name) {
        return commissionService.getByName(name);
    }

    @PostMapping
    public void save(@RequestBody Сommission commission) {
        commissionService.save(commission);
    }

    @DeleteMapping("/{name}")
    public void delete(@PathVariable String name) {
        commissionService.delete(name);
    }
}
