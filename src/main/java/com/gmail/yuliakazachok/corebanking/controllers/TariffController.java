package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.AvailableTariff;
import com.gmail.yuliakazachok.corebanking.entities.Tariff;
import com.gmail.yuliakazachok.corebanking.services.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tariff")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<Tariff> getAll() {
        return tariffService.getAll();
    }

    @GetMapping("/{id}")
    public Tariff getById(@PathVariable Integer id) {
        return tariffService.getById(id);
    }

    @GetMapping("/availabletariff/{numberPassport}")
    public List<Tariff> getAllTariffsByPassport(@PathVariable Long numberPassport) {
        return tariffService.getAllTariffsByPassport(numberPassport);
    }

    @GetMapping("/availabletariff/{numberPassport}/not")
    public List<Tariff> getAllTariffsNotByPassport(@PathVariable Long numberPassport) {
        return tariffService.getAllTariffsNotByPassport(numberPassport);
    }

    @PostMapping
    public void saveTariff(@RequestBody Tariff tariff) {
        tariffService.saveTariff(tariff);
    }

    @PostMapping("/availabletariff")
    public void saveAvailableTariff(@RequestBody AvailableTariff availableTariff) {
        tariffService.saveAvailableTariff(availableTariff);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tariffService.delete(id);
    }
}
