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
    NOT_NULL_PARAMS(BAD_REQUEST, "존재하지 않는 출석 세션입니다."),
    INVALID_SOCIAL_TYPE(BAD_REQUEST, "유효하지 않는 type 입니다."),
    INVALID_ID_TOKEN(BAD_REQUEST, "잘못된 토큰이 전달되었습니다."),
    INVALID_SOCIAL_CODE(BAD_REQUEST, "코드가 유효하지 않습니다."),
    FAILURE_READ_PRIVATE_KEY(BAD_REQUEST, "Private key 읽기 실패"),
    // 401
    UNREGISTERED_TEAM(UNAUTHORIZED, "등록되지 않은 팀입니다."),
    // 404
    NOT_FOUND_USER_SOCIAL_IDENTITY_INFO(NOT_FOUND, "등록된 소셜 정보가 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
