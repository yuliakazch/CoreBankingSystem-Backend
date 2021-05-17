package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.yuliakazachok.corebanking.dto.PaymentScheduleStates;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "payment_schedule")
@IdClass(KeyPayment.class)
public class PaymentSchedule {

    @Id
    @Column(name = "id_credit")
    @JsonProperty("id_credit")
    private Integer idCredit;

    @Id
    @Column(name = "date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @Column(name = "sum")
    private Float sum;

    @Column(name = "state")
    private PaymentScheduleStates state;

    public PaymentSchedule(Integer idCredit, Date date, float sum) {
        this.idCredit = idCredit;
        this.date = date;
        this.sum = sum;
    }

    public PaymentSchedule() {}
}
