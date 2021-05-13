package com.gmail.yuliakazachok.corebanking.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientFilters {

    private String fio;

    private Integer year;

    private List<Integer> state;
}
