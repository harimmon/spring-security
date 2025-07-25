package com.metacoding.securityapp1.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class Jwt403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(403);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String responseBody = RespFilterUtil.fail(403, accessDeniedException.getMessage());
        out.println(responseBody);
        out.flush();
    }
}
