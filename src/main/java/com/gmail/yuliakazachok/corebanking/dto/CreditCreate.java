package com.gmail.yuliakazachok.corebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class CreditCreate {

    @JsonProperty("number_passport")
    private Long numberPassport;

    @JsonProperty("id_tariff")
    private Integer idTariff;

    private Integer term;

    private Integer sum;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("date_open")
    private Date dateOpen;
}
