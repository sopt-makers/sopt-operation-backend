package org.sopt.makers.operation.code.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements SuccessCode {

    // 200
    SUCCESS_GET_USER(OK, "유저 정보 조회 성공"),
    SUCCESS_GET_USERS(OK, "전체 유저 정보 조회 성공"),

    // 204
    SUCCESS_PUT_USER(NO_CONTENT, "유저 정보 수정 성공")
    ;

    private final HttpStatus status;
    private final String message;

}
