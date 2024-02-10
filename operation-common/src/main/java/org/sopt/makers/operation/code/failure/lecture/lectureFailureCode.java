package org.sopt.makers.operation.code.failure.lecture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum lectureFailureCode implements FailureCode {
    NOT_STARTED_NTH_ATTENDANCE(BAD_REQUEST, "차 출석 시작 전입니다."),
    INVALID_ATTENDANCE(BAD_REQUEST,"존재하지 않는 출석 세션입니다."),
    ENDED_ATTENDANCE(BAD_REQUEST, "차 출석이 이미 종료되었습니다."),
    INVALID_COUNT_SESSION(BAD_REQUEST,"세션의 개수가 올바르지 않습니다."),
    INVALID_LECTURE(BAD_REQUEST,"존재하지 않는 세션입니다."),
    NO_SESSION(BAD_REQUEST,"오늘 세션이 없습니다."),
    NOT_STARTED_ATTENDANCE(BAD_REQUEST,"출석 시작 전입니다."),
    END_LECTURE(BAD_REQUEST,"이미 종료된 세션입니다.");

    private final HttpStatus status;
    private final String message;
}
