package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import com.gmail.yuliakazachok.corebanking.dto.ClientFilters;
import com.gmail.yuliakazachok.corebanking.repositories.ClientRepository;
import com.gmail.yuliakazachok.corebanking.dto.ClientStates;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                    client.setState(ClientStates.STATE_BLOCKED);
                }
        );
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public List<Client> search(ClientFilters filters) {
        String fio = filters.getFio();
        Integer year = filters.getYear();
        Date dateAfter;
        Date dateBefore;
        List<ClientStates> state = filters.getState();
        if (fio == null) {
            fio = "";
        }
        if (year == null) {
            dateAfter = Date.valueOf(LocalDate.of(1900, 1, 1));
            dateBefore = Date.valueOf(LocalDate.now());
        } else {
            dateAfter = Date.valueOf(LocalDate.of(year, 1, 1));
            dateBefore = Date.valueOf(LocalDate.of(year + 1, 1, 1));
        }
        if (state == null) {
            state = Arrays.stream(ClientStates.values()).collect(Collectors.toList());
        }
        return clientRepository.findAllByDateBirthAfterAndDateBirthBeforeAndFioContainingIgnoreCaseAndStateIn(dateAfter, dateBefore, fio, state);
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
                        client.setState(ClientStates.STATE_YES_CREDIT);
                    }
                }
        );
    }
}
