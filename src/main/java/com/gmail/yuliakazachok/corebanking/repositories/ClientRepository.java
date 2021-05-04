package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.Client;
import com.gmail.yuliakazachok.corebanking.entities.ClientFilters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c where (lower(c.fio) like concat('%', lower(:#{#filters.fio}), '%')) " +
            "and ((:#{#filters.year}) is null or extract(year from c.dateBirth) = :#{#filters.year}) " +
            "and ((:#{#filters.state}) is null or c.state in (:#{#filters.state}))")
    List<Client> searchByParamsWithFio(@Param("filters") ClientFilters filters);

    @Query("select c from Client c where ((:#{#filters.year}) is null or extract(year from c.dateBirth) = :#{#filters.year}) " +
            "and ((:#{#filters.state}) is null or c.state in (:#{#filters.state}))")
    List<Client> searchByParams(@Param("filters") ClientFilters filters);

    @Query("select c from Client c where c.state = :state")
    List<Client> findAllByState(Integer state);
}
