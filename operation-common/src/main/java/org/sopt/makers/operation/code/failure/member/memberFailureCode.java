package org.sopt.makers.operation.code.failure.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum memberFailureCode implements FailureCode {
    INVALID_MEMBER(BAD_REQUEST, "존재하지 않는 회원입니다.");

    private final HttpStatus status;
    private final String message;
}
