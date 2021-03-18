package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.Сommission;
import com.gmail.yuliakazachok.corebanking.repositories.СommissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class СommissionService {

    private final СommissionRepository commissionRepository;

    public List<Сommission> getAll() {
        return commissionRepository.findAll();
    }

    public void save(Сommission commission) {
        commissionRepository.save(commission);
    }
}
