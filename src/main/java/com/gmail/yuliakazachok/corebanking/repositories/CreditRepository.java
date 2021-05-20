package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.states.CreditStates;
import com.gmail.yuliakazachok.corebanking.entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {

    Credit findCreditByStateAndIdAvailTariffIn(CreditStates state, List<Integer> ids);

    List<Credit> findAllByStateAndIdAvailTariffIn(CreditStates state, List<Integer> idsAvailTariff);
}
