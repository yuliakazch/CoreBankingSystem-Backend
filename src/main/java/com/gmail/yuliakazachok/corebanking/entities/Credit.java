package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.yuliakazachok.corebanking.entities.states.CreditStates;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_avail_tariff")
    @JsonProperty("id_avail_tariff")
    private Integer idAvailTariff;

    @Column(name = "date_open")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("date_open")
    private Date dateOpen;

    @Column(name = "term")
    private Integer term;

    @Column(name = "sum")
    private Integer sum;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private CreditStates state;

    @Column(name = "balance")
    private Float balance;

    public Credit(Integer idAvailTariff, Date dateOpen, Integer term, Integer sum, CreditStates state) {
        this.idAvailTariff = idAvailTariff;
        this.dateOpen = dateOpen;
        this.term = term;
        this.sum = sum;
        this.state = state;
    }

    public Credit() {}
}
