package org.sopt.makers.operation.code.failure.subAttendance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum subAttendanceFailureCode implements FailureCode {
    INVALID_SUB_LECTURE(BAD_REQUEST, "존재하지 않는 세션입니다."),
    INVALID_CODE(BAD_REQUEST, "코드가 일치하지 않아요!");

    private final HttpStatus status;
    private final String message;
}
