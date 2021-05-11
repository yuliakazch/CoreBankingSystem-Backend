package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "payments")
@IdClass(KeyPayment.class)
public class Payment {

    @Id
    @Column(name = "id_credit")
    @JsonProperty("id_credit")
    private Integer idCredit;

    @Id
    @Column(name = "date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @Column(name = "id_commission")
    @JsonProperty("id_commission")
    private Integer idCommission;

    @Column(name = "sum")
    private Float sum;
}
