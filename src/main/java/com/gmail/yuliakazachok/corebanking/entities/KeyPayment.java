package com.gmail.yuliakazachok.corebanking.entities;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class KeyPayment implements Serializable {

    private Integer idCredit;

    private Date date;
}
