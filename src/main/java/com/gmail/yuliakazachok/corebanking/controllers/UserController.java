package com.gmail.yuliakazachok.corebanking.controllers;

import com.gmail.yuliakazachok.corebanking.config.jwt.JwtProvider;
import com.gmail.yuliakazachok.corebanking.dto.AuthRequest;
import com.gmail.yuliakazachok.corebanking.dto.AuthResponse;
import com.gmail.yuliakazachok.corebanking.entities.User;
import com.gmail.yuliakazachok.corebanking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public void registerUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.getUserByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }
}
