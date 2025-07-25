package com.metacoding.securityapp1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public @ResponseBody String adminMain() {
        return "<h1>admin page</h1>";
    }
}