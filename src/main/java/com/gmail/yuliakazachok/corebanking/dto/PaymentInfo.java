package com.gmail.yuliakazachok.corebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
public class PaymentInfo {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;

    private Float sum;

    private Integer interest;

    public PaymentInfo(Date date, Float sum, Integer interest) {
        this.date = date;
        this.sum = sum;
        this.interest = interest;
    }

    public PaymentInfo() {}
}
