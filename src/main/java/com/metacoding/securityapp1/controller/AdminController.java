package com.metacoding.securityapp1.controller;

import com.metacoding.securityapp1.core.Resp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping
    public Resp<?> adminMain() {
        return new Resp<>();
    }
}