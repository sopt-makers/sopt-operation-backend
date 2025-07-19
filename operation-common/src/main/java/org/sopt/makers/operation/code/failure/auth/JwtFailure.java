package org.sopt.makers.operation.code.failure.auth;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum JwtFailure implements FailureCode {
    JWT_MISSING_AUTH_HEADER(HttpStatus.UNAUTHORIZED, "인증 헤더가 존재하지 않습니다."),
    JWT_PARSE_FAILED(HttpStatus.UNAUTHORIZED, "잘못된 형식의 JWT입니다."),
    JWT_INVALID_CLAIMS(HttpStatus.UNAUTHORIZED, "JWT의 클레임이 유효하지 않습니다."),
    JWT_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT 검증에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}

