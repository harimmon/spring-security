package com.metacoding.securityapp1.core;

import com.metacoding.securityapp1.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 전용 필터
 * - 클라이언트 요청의 Authorization 헤더에 있는 JWT를 파싱하고
 * - 유효하면 Spring Security의 인증 객체(Authentication)을 생성해서 저장함
 * - 이 필터는 OncePerRequestFilter를 상속받아 요청마다 단 한 번만 실행됨
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    /**
     * 매 요청마다 실행되는 메서드
     *
     * @param request     요청 객체
     * @param response    응답 객체
     * @param filterChain 다음 필터로 전달하는 체인
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 JWT 토큰 추출
        String jwt = request.getHeader(JwtUtil.HEADER); // ex: "Authorization: Bearer <token>"

        // 2. 토큰이 없거나 "Bearer " 접두사가 없으면 → 필터 통과 (인증 처리 안 함)
        if (jwt == null || !jwt.startsWith(JwtUtil.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response); // 다음 필터로 넘김
            return;
        }

        try {
            // 3. "Bearer " 접두사 제거
            jwt = jwt.replace(JwtUtil.TOKEN_PREFIX, "");

            // 4. JWT 유효성 검사 및 User 객체 추출
            User user = JwtUtil.verify(jwt); // id, username, roles 포함된 객체 반환

            // 5. 인증 객체 생성 (Password는 null, 권한은 user.getAuthorities()로 전달)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            // 6. 인증 객체를 SecurityContextHolder에 저장 → 이후 인증된 상태로 인식됨
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // 예외 발생 시 콘솔에 출력 (실무에서는 로깅 또는 응답 처리 필요)
            System.out.println("JWT 오류 : " + e.getMessage());
        }

        // 7. 필터 체인 계속 진행 (이 필터 다음으로 넘김)
        filterChain.doFilter(request, response);
    }
}
