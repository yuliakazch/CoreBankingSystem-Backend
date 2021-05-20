package com.gmail.yuliakazachok.corebanking.entities;

import com.gmail.yuliakazachok.corebanking.entities.role.UserRole;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "fio")
    private String fio;

    @Column(name = "position")
    private String position;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "reset_phrase")
    private String resetPhrase;
}
