package com.gmail.yuliakazachok.corebanking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "min_sum")
    @JsonProperty("min_sum")
    private Integer minSum;

    @Column(name = "max_sum")
    @JsonProperty("max_sum")
    private Integer maxSum;

    @Column(name = "min_term")
    @JsonProperty("min_term")
    private Integer minTerm;

    @Column(name = "max_term")
    @JsonProperty("max_term")
    private Integer maxTerm;
}
