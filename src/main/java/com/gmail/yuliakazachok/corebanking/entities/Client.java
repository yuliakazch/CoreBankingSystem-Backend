package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.yuliakazachok.corebanking.dto.ClientStates;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @Column(name = "number_passport")
    @JsonProperty("number_passport")
    private Long numberPassport;

    @Column(name = "fio")
    private String fio;

    @Column(name = "date_birth")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("date_birth")
    private Date dateBirth;

    @Column(name = "place")
    private String place;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private ClientStates state;

    @Column(name = "count_block_days")
    @JsonProperty("count_block_days")
    private Integer countBlockDays;
}
