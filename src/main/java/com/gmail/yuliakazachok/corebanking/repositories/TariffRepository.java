package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    Tariff findTariffById(Integer id);

    List<Tariff> findAllByIdIn(List<Integer> ids);

    List<Tariff> findAllByIdNotIn(List<Integer> ids);
}
