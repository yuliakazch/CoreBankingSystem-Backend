package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.Сommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface СommissionRepository extends JpaRepository<Сommission, String> {

    @Query("select c from Сommission c where c.name = :name")
    Сommission getByName(@Param("name") String name);
}
