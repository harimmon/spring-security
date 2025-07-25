package com.metacoding.securityapp1.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 필터 또는 예외 핸들러에서 JSON 형식으로 에러 응답을 만들기 위한 유틸 클래스
 * -> 예: Spring Security 필터에서 실패 응답으로 JSON 바디를 내려줄 때 사용
 */
public class RespFilterUtil {

    // Jackson의 ObjectMapper: 자바 객체 → JSON 문자열로 변환할 때 사용
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 실패 응답 객체를 JSON 문자열로 만들어 반환
     *
     * @param status HTTP 상태 코드 (ex: 401, 403, 400 등)
     * @param msg    에러 메시지 내용
     * @return JSON 형태의 실패 응답 문자열 (ex: {"status":401,"msg":"인증 실패"})
     */
    public static String fail(Integer status, String msg) {
        // Resp는 공통 응답 DTO. 제네릭으로 감싸되, 데이터는 없으므로 <?>
        Resp<?> resp = new Resp<>(status, msg);
        try {
            // 자바 객체 resp → JSON 문자열로 직렬화
            return mapper.writeValueAsString(resp);
        } catch (JsonProcessingException e) {
            // 변환 실패 시 예외 발생 (실무에서는 로깅 + 사용자 정의 예외 처리 고려)
            throw new RuntimeException("json 변환 실패");
        }
    }
}
