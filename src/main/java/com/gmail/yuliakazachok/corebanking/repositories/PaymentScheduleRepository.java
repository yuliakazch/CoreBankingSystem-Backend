package com.gmail.yuliakazachok.corebanking.repositories;

import com.gmail.yuliakazachok.corebanking.entities.states.PaymentScheduleStates;
import com.gmail.yuliakazachok.corebanking.entities.KeyPayment;
import com.gmail.yuliakazachok.corebanking.entities.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, KeyPayment> {

    List<PaymentSchedule> findAllByIdCreditOrderByDate(Integer idCredit);

    List<PaymentSchedule> findAllByIdCreditAndStateInOrderByDate(Integer idCredit, List<PaymentScheduleStates> states);
}
