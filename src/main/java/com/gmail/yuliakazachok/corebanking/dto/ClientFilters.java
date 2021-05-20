package com.gmail.yuliakazachok.corebanking.dto;

import com.gmail.yuliakazachok.corebanking.entities.states.ClientStates;
import lombok.Data;

import java.util.List;

@Data
public class ClientFilters {

    private String fio;

    private Integer year;

    private List<ClientStates> state;
}
