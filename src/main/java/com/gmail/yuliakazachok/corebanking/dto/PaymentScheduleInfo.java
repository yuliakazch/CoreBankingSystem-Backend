package com.gmail.yuliakazachok.corebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.yuliakazachok.corebanking.entities.states.PaymentScheduleStates;
import lombok.Data;

import java.sql.Date;

@Data
public class PaymentScheduleInfo {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;

    private Float sum;

    private PaymentScheduleStates state;

    public PaymentScheduleInfo(Date date, Float sum, PaymentScheduleStates state) {
        this.date = date;
        this.sum = sum;
        this.state = state;
    }

    public PaymentScheduleInfo() {}
}
