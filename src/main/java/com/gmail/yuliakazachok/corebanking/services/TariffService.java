package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.Tariff;
import com.gmail.yuliakazachok.corebanking.repositories.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;

    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }

    public Optional<Tariff> getById(Integer id) {
        return tariffRepository.findById(id);
    }

    public void save(Tariff tariff) {
        tariffRepository.save(tariff);
    }

    public void delete(Integer id) {
        tariffRepository.deleteById(id);
    }
}
