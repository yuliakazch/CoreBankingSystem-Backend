package com.gmail.yuliakazachok.corebanking.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String login;

    private String password;
}
