package org.sopt.makers.operation.code.failure.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum AdminFailureCode implements FailureCode {
    DUPLICATED_EMAIL(BAD_REQUEST,"중복되는 이메일입니다."),
    INVALID_EMAIL(BAD_REQUEST,"이메일이 존재하지 않습니다."),
    INVALID_PASSWORD(BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    NOT_APPROVED_ACCOUNT(BAD_REQUEST,"승인되지 않은 계정입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST,"유효하지 않은 리프레시 토큰입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
