package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.Сommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface СommissionRepository extends JpaRepository<Сommission, Integer> {

    Сommission findCommissionById(Integer id);
}
