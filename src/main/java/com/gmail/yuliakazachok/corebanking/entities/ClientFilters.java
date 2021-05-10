package com.gmail.yuliakazachok.corebanking.entities;

import lombok.Data;

import java.util.List;

@Data
public class ClientFilters {

    private String fio;

    private Integer year;

    private List<Integer> state;
}
