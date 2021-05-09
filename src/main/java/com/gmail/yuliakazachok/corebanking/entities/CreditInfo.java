package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class CreditInfo {

    private Integer id;

    @JsonProperty("number_passport")
    private Long numberPassport;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("date_open")
    private Date dateOpen;

    private Integer rate;

    private Integer term;

    private Integer sum;

    private Integer state;

    public CreditInfo(Integer id, Long numberPassport, Date dateOpen, Integer rate, Integer term, Integer sum, Integer state) {
        this.id = id;
        this.numberPassport = numberPassport;
        this.dateOpen = dateOpen;
        this.rate = rate;
        this.term = term;
        this.sum = sum;
        this.state = state;
    }
}
