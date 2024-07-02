package org.sopt.makers.operation.code.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum UserFailureCode implements FailureCode{

    // 400
    INVALID_PARAMETER(BAD_REQUEST, "잘못된 형식의 회원 정보 요청입니다."),
    INVALID_USER_INCLUDED(BAD_REQUEST, "유효하지 않은 회원 ID가 포함되어 있습니다."),

    // 404
    NOT_FOUND_USER(NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_FOUND_USER_INCLUDED(NOT_FOUND, "존재하지 않는 회원 ID가 포함되어 있습니다."),
    NOT_FOUND_HISTORY(NOT_FOUND, "해당 유저의 활동 이력을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

}
