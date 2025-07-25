package com.metacoding.securityapp1.controller;

import com.metacoding.securityapp1.core.Resp;
import com.metacoding.securityapp1.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public Resp<?> user() {
        return new Resp<>();
    }
}