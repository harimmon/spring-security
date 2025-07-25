package com.metacoding.securityapp1.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 예: localhost:8080/user/asdasd, localhost:8080/user/join-form
@Configuration // 이 클래스는 스프링 설정 클래스임을 명시 (빈 등록 용도)
public class SecurityConfig {

    // 비밀번호 암호화를 위한 단방향 해시 함수 (회원가입/로그인 시 사용)
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 컨텍스트 홀더에 세션 저장할 때 사용하는 클래스
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean // SecurityFilterChain을 빈으로 등록하여 보안 설정을 적용함
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // H2 Console은 iframe으로 동작하므로, 기본적으로 막혀있는 iframe을 sameOrigin으로 허용함
        // 1. iframe 하용 -> mysql로 전환하면 삭제
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // CSRF 보호 기능 비활성화 (개발/테스트용. 실서비스에서는 활성화 권장)
        // 2. csrf 비활성화 -> html 사용 안 할 거니까 !
        http.csrf(csrf -> csrf.disable());

        // 3. 세션 비활성화 (STATELESS) -> 키를  전달 안 해주고, 집에 갈 때 락카를 비워버린다.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4. 폼 로그인 비활성화 (UsernamePasswordAuthenticationFilter 발동을 막기)
        http.formLogin(form -> form.disable());

        // 5. HTTP Basic 인증 비활성화 (BasicAuthenticationFilter 발동을 막기)
        http.httpBasic(basicLogin -> basicLogin.disable());

        // 6. 커스텀 필터 장착 (인가 필터) -> 로그인 컨트롤러에서 직접하기
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 7. 예외처리 핸들러 등록 (1. 인증, 인가가 완료되면 어떻게 ? 후처리, 2. 예외가 발생하면 어떻게 ?)
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(new Jwt401Handler())
                .accessDeniedHandler(new Jwt403Handler()));

        // 요청 URL별 인가 설정
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/user/**").hasRole("USER") // USER 권한 있어야 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한 있어야 접근 가능
                        .anyRequest().permitAll() // 그 외 모든 요청은 인증 없이 접근 허용
        );

        return http.build(); // 구성된 보안 필터 체인 객체를 반환
    }
}
