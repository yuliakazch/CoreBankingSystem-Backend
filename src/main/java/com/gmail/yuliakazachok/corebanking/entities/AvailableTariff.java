package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "available_tariffs")
public class AvailableTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_tariff")
    @JsonProperty("id_tariff")
    private Integer idTariff;

    @Column(name = "number_passport")
    @JsonProperty("number_passport")
    private Long numberPassport;
}
