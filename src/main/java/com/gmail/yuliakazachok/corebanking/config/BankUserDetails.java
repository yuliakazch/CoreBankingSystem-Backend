package com.gmail.yuliakazachok.corebanking.config;

import com.gmail.yuliakazachok.corebanking.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class BankUserDetails implements UserDetails {

    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static BankUserDetails fromUserToBankUserDetails(User user) {
        BankUserDetails bankUserDetails = new BankUserDetails();
        bankUserDetails.login = user.getLogin();
        bankUserDetails.password = user.getPassword();
        bankUserDetails.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        return bankUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
