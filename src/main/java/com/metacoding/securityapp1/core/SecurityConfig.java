package com.metacoding.securityapp1.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 예: localhost:8080/user/asdasd, localhost:8080/user/join-form
@Configuration // 이 클래스는 스프링 설정 클래스임을 명시 (빈 등록 용도)
public class SecurityConfig {

    // 비밀번호 암호화를 위한 단방향 해시 함수 (회원가입/로그인 시 사용)
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean // SecurityFilterChain을 빈으로 등록하여 보안 설정을 적용함
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // H2 Console은 iframe으로 동작하므로, 기본적으로 막혀있는 iframe을 sameOrigin으로 허용함
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // CSRF 보호 기능 비활성화 (개발/테스트용. 실서비스에서는 활성화 권장)
        http.csrf(configure -> configure.disable());

        // 폼 로그인 방식 설정
        http.formLogin(form -> form
                .loginPage("/login-form")           // 로그인 페이지 URL (GET). 사용자가 인증 안 됐을 때 여기로 이동함
                .loginProcessingUrl("/login")       // 로그인 처리 URL (POST). 컨트롤러 없이도 Spring Security가 자동으로 인증 처리
                .defaultSuccessUrl("/main")         // 로그인 성공 후 이동할 기본 페이지
        );

        // 요청 URL별 인가 설정
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/main").authenticated() // 로그인만 되어 있으면 접근 가능
                        .requestMatchers("/user/**").hasRole("USER") // USER 권한 있어야 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한 있어야 접근 가능
                        .anyRequest().permitAll() // 그 외 모든 요청은 인증 없이 접근 허용
        );

        return http.build(); // 구성된 보안 필터 체인 객체를 반환
    }
}
