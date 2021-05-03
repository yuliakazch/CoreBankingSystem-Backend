package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import com.gmail.yuliakazachok.corebanking.entities.ClientFilters;
import com.gmail.yuliakazachok.corebanking.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> getByPassport(Long numberPassport) {
        return clientRepository.findById(numberPassport);
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
}
