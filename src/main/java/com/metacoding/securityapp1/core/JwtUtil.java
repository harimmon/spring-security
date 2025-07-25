package com.metacoding.securityapp1.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.metacoding.securityapp1.domain.user.User;

import java.util.Date;

/**
 * JWT 토큰을 생성하고 검증하는 유틸리티 클래스
 * JWT 라이브러리는 Auth0 JWT(Java JWT: https://github.com/auth0/java-jwt)를 사용
 */
public class JwtUtil {

    // Authorization 헤더 이름: 클라이언트에서 JWT를 보낼 때 이 이름으로 보냄
    public static final String HEADER = "Authorization";

    // 토큰 앞에 붙는 접두어. Spring Security에서는 "Bearer " 형식으로 기대함
    public static final String TOKEN_PREFIX = "Bearer ";

    // 서명용 시크릿 키. 실무에서는 반드시 외부에 노출되지 않게 환경변수로 관리
    public static final String SECRET = "메타코딩시크릿키";

    // 토큰 유효 시간 (7일). System.currentTimeMillis() 기준으로 계산
    public static final Long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    /**
     * JWT 생성 메서드
     *
     * @param user 로그인된 사용자 객체
     * @return Bearer 토큰 문자열
     */
    public static String create(User user) {
        String jwt = JWT.create()
                .withSubject(user.getUsername()) // 토큰의 제목(subject) 설정 (보통 유저명, 고유식별자)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시점 설정
                .withClaim("id", user.getId()) // 커스텀 클레임: id
                .withClaim("roles", user.getRoles()) // 커스텀 클레임: 역할
                .sign(Algorithm.HMAC512(SECRET)); // HMAC512 알고리즘 + 시크릿 키로 서명

        return TOKEN_PREFIX + jwt; // "Bearer " + 실제 토큰 형태로 반환
    }

    /**
     * JWT 검증 및 디코딩 메서드
     *
     * @param jwt 클라이언트가 보낸 JWT (Bearer 접두사 제거 후)
     * @return 토큰에서 추출한 유저 정보로 구성한 User 객체
     */
    public static User verify(String jwt) {
        // 시크릿 키 기반으로 디코더 설정 + 검증 수행 (시그니처 위조 여부 확인 포함)
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET))
                .build()
                .verify(jwt); // JWT 문자열을 디코딩 + 유효성 검사 수행

        // 클레임에서 필요한 정보 추출
        Integer id = decodedJWT.getClaim("id").asInt(); // 커스텀 클레임 - ID
        String username = decodedJWT.getSubject();      // subject에 저장한 username
        String roles = decodedJWT.getClaim("roles").asString(); // 역할 정보

        // 토큰 정보를 기반으로 새 User 객체 생성 (실제 인증 객체로 활용할 수 있음)
        return User.builder()
                .id(id)
                .username(username)
                .roles(roles)
                .build();
    }
}
