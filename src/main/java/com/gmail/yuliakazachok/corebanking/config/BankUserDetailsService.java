package com.gmail.yuliakazachok.corebanking.config;

import com.gmail.yuliakazachok.corebanking.entities.User;
import com.gmail.yuliakazachok.corebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public BankUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.getUserByLogin(login);
        return BankUserDetails.fromUserToBankUserDetails(user);
    }
}
