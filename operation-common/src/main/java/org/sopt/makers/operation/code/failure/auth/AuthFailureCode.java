package org.sopt.makers.operation.code.failure.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum AuthFailureCode implements FailureCode {
    // 400
    NOT_NULL_GRANT_TYPE(BAD_REQUEST, "grantType 데이터가 들어오지 않았습니다."),
    INVALID_SOCIAL_TYPE(BAD_REQUEST, "유효하지 않은 social type 입니다."),
    INVALID_ID_TOKEN(BAD_REQUEST, "유효하지 않은 id token 입니다."),
    INVALID_SOCIAL_CODE(BAD_REQUEST, "유효하지 않은 social code 입니다."),
    FAILURE_READ_PRIVATE_KEY(BAD_REQUEST, "Private key 읽기 실패"),
    INVALID_GRANT_TYPE(BAD_REQUEST, "유효하지 않은 grantType 입니다."),
    NOT_NULL_CODE(BAD_REQUEST, "플랫폼 인가코드가 들어오지 않았습니다."),
    USED_PLATFORM_CODE(BAD_REQUEST, "이미 사용한 플랫폼 인가코드입니다."),
    NOT_NULL_REFRESH_TOKEN(BAD_REQUEST, "리프레쉬 토큰이 들어오지 않았습니다."),
    // 401
    EXPIRED_PLATFORM_CODE(UNAUTHORIZED, "만료된 플랫폼 인가 코드입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "만료된 리프레쉬 토큰입니다."),
    // 404
    NOT_FOUNT_REGISTERED_TEAM(NOT_FOUND, "등록되지 않은 팀입니다."),
    NOT_FOUND_USER_SOCIAL_IDENTITY_INFO(NOT_FOUND, "등록된 소셜 정보가 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
