package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.AvailableTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableTariffRepository extends JpaRepository<AvailableTariff, Integer> {

    List<AvailableTariff> findAllByNumberPassport(Long numberPassport);

    AvailableTariff findByNumberPassportAndIdTariff(Long numberPassport, Integer idTariff);
}
