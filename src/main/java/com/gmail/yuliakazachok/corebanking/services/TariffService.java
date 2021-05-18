package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.AvailableTariff;
import com.gmail.yuliakazachok.corebanking.entities.Tariff;
import com.gmail.yuliakazachok.corebanking.repositories.AvailableTariffRepository;
import com.gmail.yuliakazachok.corebanking.repositories.ClientRepository;
import com.gmail.yuliakazachok.corebanking.repositories.TariffRepository;
import com.gmail.yuliakazachok.corebanking.dto.ClientStates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;
    private final AvailableTariffRepository availableTariffRepository;
    private final ClientRepository clientRepository;

    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }

    public Tariff getById(Integer id) {
        return tariffRepository.findById(id).get();
    }

    public List<Tariff> getAllTariffsByPassport(Long numberPassport) {
        List<Integer> idTariffs = availableTariffRepository.findAllByNumberPassport(numberPassport)
                .stream().map(AvailableTariff::getIdTariff).collect(Collectors.toList());
        return tariffRepository.findAllByIdIn(idTariffs);
    }

    public List<Tariff> getAllTariffsNotByPassport(Long numberPassport) {
        List<Integer> idTariffs = availableTariffRepository.findAllByNumberPassport(numberPassport)
                .stream().map(AvailableTariff::getIdTariff).collect(Collectors.toList());
        if (idTariffs.isEmpty()) {
            return tariffRepository.findAll();
        } else {
            return tariffRepository.findAllByIdNotIn(idTariffs);
        }
    }

    public void saveTariff(Tariff tariff) {
        tariffRepository.save(tariff);
    }

    @Transactional
    public void saveAvailableTariff(AvailableTariff availableTariff) {
        availableTariffRepository.save(availableTariff);
        clientRepository.findById(availableTariff.getNumberPassport()).ifPresent(client -> {
                    if (client.getState() == ClientStates.STATE_NOT_TARIFF) {
                        client.setState(ClientStates.STATE_NOT_CREDIT);
                    }
                }
        );
    }

    public void delete(Integer id) {
        tariffRepository.deleteById(id);
    }
}
