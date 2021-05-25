package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import com.gmail.yuliakazachok.corebanking.dto.ClientFilters;
import com.gmail.yuliakazachok.corebanking.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{numberPassport}")
    public Client getByPassport(@PathVariable Long numberPassport) {
        return clientService.getByPassport(numberPassport);
    }

    @GetMapping("/block")
    public void block(@RequestParam("number") Long number, @RequestParam("days") Integer countDays) {
        clientService.block(number, countDays);
    }

    @GetMapping("/unblock")
    public void unblock(@RequestParam("number") Long number) {
        clientService.unblock(number);
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
