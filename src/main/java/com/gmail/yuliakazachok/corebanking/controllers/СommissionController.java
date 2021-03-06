package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.Сommission;
import com.gmail.yuliakazachok.corebanking.services.СommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commission")
@RequiredArgsConstructor
public class СommissionController {

    private final СommissionService commissionService;

    @GetMapping
    public List<Сommission> getAll() {
        return commissionService.getAll();
    }

    @GetMapping("/{id}")
    public Сommission getById(@PathVariable Integer id) {
        return commissionService.getById(id);
    }

    @PostMapping
    public void save(@RequestBody Сommission commission) {
        commissionService.save(commission);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        commissionService.delete(id);
    }
}
