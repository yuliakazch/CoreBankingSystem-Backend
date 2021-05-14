package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.KeyPayment;
import com.gmail.yuliakazachok.corebanking.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, KeyPayment> {

    List<Payment> findAllByIdCreditOrderByDate(Integer idCredit);
}
