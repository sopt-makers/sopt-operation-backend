package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AlarmFailureCode implements FailureCode {
    FAIL_SEND_ALARM(BAD_REQUEST, "알림 즉시 발송에 실패하였습니다."),
    FAIL_SCHEDULE_ALARM(BAD_REQUEST, "알림 예약 발송에 실패하였습니다."),
    INVALID_ALARM(NOT_FOUND, "알림이 존재하지 않습니다."),
    FAIL_INACTIVE_USERS(BAD_REQUEST, "비활동 유저 불러오기에 실패하였습니다."),
    INVALID_ALARM_TARGET_TYPE(BAD_REQUEST, "올바르지 않은 알림 타겟 타입입니다."),
    INVALID_SCHEDULE_ALARM_FORMAT(BAD_REQUEST, "알림 예약 시간 포맷이 맞지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
