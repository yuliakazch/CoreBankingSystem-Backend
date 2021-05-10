package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByState(Integer state);

    List<Client> findAllByDateBirthAfterAndDateBirthBeforeAndFioContainingIgnoreCaseAndStateIn(Date dateAfter, Date dateBefore, String fio, List<Integer> state);
}
