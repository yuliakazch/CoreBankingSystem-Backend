package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import com.gmail.yuliakazachok.corebanking.entities.ClientFilters;
import com.gmail.yuliakazachok.corebanking.services.ClientService;
import com.gmail.yuliakazachok.corebanking.utils.paths.ClientPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ClientPaths.CONTROLLER)
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{numberPassport}")
    public Client getByPassport(@PathVariable Long numberPassport) {
        return clientService.getByPassport(numberPassport).get();
    }

    @GetMapping("/block")
    public void block(@RequestParam("number") Long number, @RequestParam("days") Integer countDays) {
        clientService.block(number, countDays);
    }

    @PostMapping
    public void save(@RequestBody Client client) {
        clientService.save(client);
    }

    @PostMapping("/search")
    public List<Client> search(@RequestBody ClientFilters filters) {
        return clientService.search(filters);
    }

    @DeleteMapping("/{numberPassport}")
    public void delete(@PathVariable Long numberPassport) {
        clientService.delete(numberPassport);
    }
}
