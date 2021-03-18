package com.gmail.yuliakazachok.corebanking.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "commissions")
public class Сommission {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "interest")
    private Integer interest;
}
