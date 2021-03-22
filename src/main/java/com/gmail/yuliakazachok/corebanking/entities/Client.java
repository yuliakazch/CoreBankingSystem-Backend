package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @JsonFormat(pattern="dd-MM-yyyy")
    @JsonProperty("date_birth")
    private Date dateBirth;

    @Column(name = "place")
    private String place;

    @Column(name = "is_locked")
    @JsonProperty("is_locked")
    private Boolean isLocked;
}
