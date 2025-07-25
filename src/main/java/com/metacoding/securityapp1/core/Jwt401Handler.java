package com.metacoding.securityapp1.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

public class Jwt401Handler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String responseBody = RespFilterUtil.fail(401, authException.getMessage());
        out.println(responseBody);
        out.flush();
    }
}
