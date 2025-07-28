package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AlarmFailureCode implements FailureCode {
    // 400
    INVALID_SEND_INSTANT_REQUEST(BAD_REQUEST, "유효하지 않은 요청 키/값이 존재합니다."),
    INVALID_SEND_SCHEDULED_REQUEST(BAD_REQUEST, "유효하지 않은 요청 키/값이 존재합니다."),
    INVALID_LINK(BAD_REQUEST, "지원하지 않는 링크입니다."),
    FAIL_INACTIVE_USERS(BAD_REQUEST, "비활동 유저 불러오기에 실패하였습니다."),
    INVALID_ALARM_TARGET_TYPE(BAD_REQUEST, "올바르지 않은 알림 타겟 타입입니다."),
    INVALID_SCHEDULE_ALARM_FORMAT(BAD_REQUEST, "알림 예약 시간 포맷이 맞지 않습니다."),
    FAIL_SEND_ALARM(BAD_REQUEST, "알림 즉시 발송에 실패하였습니다."),
    FAIL_SCHEDULE_ALARM(BAD_REQUEST, "알림 예약 발송에 실패하였습니다."),
    FAIL_DELETE_SCHEDULE_ALARM(BAD_REQUEST, "예약 알림 삭자에 실패하였습니다."),

    // 404
    NOT_FOUND_ALARM(NOT_FOUND, "알림이 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
