package com.metacoding.securityapp1.controller.dto;

import lombok.Data;

public class UserRequest {

    @Data
    public static class Login {
        private String username;
        private String password;
    }

    @Data
    public static class Join {
        private String username;
        private String password;
        private String email;
    }
}
