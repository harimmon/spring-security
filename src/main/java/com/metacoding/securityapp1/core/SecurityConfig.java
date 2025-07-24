package com.metacoding.securityapp1.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 예: localhost:8080/user/asdasd, localhost:8080/user/join-form
@Configuration // 이 클래스는 스프링 설정 파일임을 명시
public class SecurityConfig {

    // 단방향 해시
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean // SecurityFilterChain을 빈으로 등록하여 보안 설정을 적용함
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(configure -> configure.disable());

        // 폼 로그인 방식 설정
        http.formLogin(form -> form
                .loginPage("/login-form")           // 로그인 페이지 URL (GET). 사용자가 인증 안 됐을 때 여기로 이동함
                .loginProcessingUrl("/login")       // 로그인 처리 URL (POST). 이 URL로 username/password 보내면 스프링 시큐리티가 처리함
                .defaultSuccessUrl("/main")         // 로그인 성공 후 이동할 기본 페이지
        );

        // 요청 URL별 인증 설정
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/user/**", "/main").authenticated() // 이 URL들은 로그인(인증)된 사용자만 접근 가능
                        .anyRequest().permitAll()                             // 그 외 모든 요청은 인증 없이 접근 허용
        );

        return http.build(); // 구성된 보안 필터 체인 객체를 반환
    }
}
