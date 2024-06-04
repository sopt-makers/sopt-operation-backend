package org.sopt.makers.operation.code.success.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {
    SUCCESS_GET_AUTHORIZATION_CODE(OK, "플랫폼 인가코드 발급 성공"),
    SUCCESS_GENERATE_TOKEN(OK, "토큰 발급 성공");
    private final HttpStatus status;
    private final String message;
}
