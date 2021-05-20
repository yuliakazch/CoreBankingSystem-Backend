package com.gmail.yuliakazachok.corebanking.services;

import com.gmail.yuliakazachok.corebanking.entities.User;
import com.gmail.yuliakazachok.corebanking.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public User getUserByLoginAndPassword(String login, String password) {
        User user = getUserByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
