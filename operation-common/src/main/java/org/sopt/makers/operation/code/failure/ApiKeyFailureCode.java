package org.sopt.makers.operation.code.failure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@Getter
public enum ApiKeyFailureCode implements FailureCode {
    MISSING_API_KEY(BAD_REQUEST, "API 키가 누락되었습니다."),
    INVALID_API_KEY(UNAUTHORIZED, "유효하지 않은 API 키입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}