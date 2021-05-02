package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.Tariff;
import com.gmail.yuliakazachok.corebanking.services.TariffService;
import com.gmail.yuliakazachok.corebanking.utils.paths.TariffPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(TariffPaths.CONTROLLER)
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<Tariff> getAll() {
        return tariffService.getAll();
    }

    @GetMapping("/{id}")
    public Tariff getById(@PathVariable Integer id) {
        return tariffService.getById(id).get();
    }

    @PostMapping
    public void save(@RequestBody Tariff tariff) {
        tariffService.save(tariff);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tariffService.delete(id);
    }
}
