package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {

    Optional<Credit> findCreditByStateAndIdAvailTariffIn(Integer state, List<Integer> ids);

    List<Credit> findAllByStateAndIdAvailTariffIn(Integer state, List<Integer> idsAvailTariff);
}
