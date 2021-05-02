package com.gmail.yuliakazachok.corebanking.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "commissions")
public class Сommission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "interest")
    private Integer interest;
}
