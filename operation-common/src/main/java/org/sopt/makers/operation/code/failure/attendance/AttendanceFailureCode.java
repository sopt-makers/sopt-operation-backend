package org.sopt.makers.operation.code.failure.attendance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum AttendanceFailureCode implements FailureCode {
    INVALID_ATTENDANCE(BAD_REQUEST, "존재하지 않는 출석 세션입니다.");

    private final HttpStatus status;
    private final String message;
}
