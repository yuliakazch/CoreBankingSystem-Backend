package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import com.gmail.yuliakazachok.corebanking.entities.ClientFilters;
import com.gmail.yuliakazachok.corebanking.repositories.ClientRepository;
import com.gmail.yuliakazachok.corebanking.utils.ClientStates;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client getByPassport(Long numberPassport) {
        return clientRepository.findById(numberPassport).get();
    }

    @Transactional
    public void block(Long number, Integer countDays) {
        clientRepository.findById(number).ifPresent(client -> {
                    client.setCountBlockDays(countDays);
                    client.setState(ClientStates.STATE_BLOCKED.ordinal());
                }
        );
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public List<Client> search(ClientFilters filters) {
        if (filters.getFio() == null) {
            return clientRepository.searchByParams(filters);
        } else {
            return clientRepository.searchByParamsWithFio(filters);
        }
    }

    public void delete(Long numberPassport) {
        clientRepository.deleteById(numberPassport);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void decBlockDay() {
        clientRepository.findAllByState(ClientStates.STATE_BLOCKED.ordinal()).forEach(client -> {
                    int newCountDays = client.getCountBlockDays() - 1;
                    client.setCountBlockDays(newCountDays);
                    if (newCountDays == 0) {
                        client.setState(ClientStates.STATE_YES_CREDIT.ordinal());
                    }
                }
        );
    }
}
