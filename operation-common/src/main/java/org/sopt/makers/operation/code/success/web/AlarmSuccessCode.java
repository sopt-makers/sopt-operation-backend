package org.sopt.makers.operation.code.success.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum AlarmSuccessCode implements SuccessCode {
    // 200
    SUCCESS_GET_ALARM(OK, "알림 상세 조회 성공"),
    SUCCESS_GET_ALARMS(OK, "알림 리스트 조회 성공"),

    // 201
    SUCCESS_SEND_ALARM(CREATED, "알림 즉시 발송 성공"),
    SUCCESS_SCHEDULE_ALARM(CREATED, "알림 예약 발송 성공"),

    // 204
    SUCCESS_UPDATE_ALARM_STATUS(NO_CONTENT, "알림 상태 업데이트 성공"),
    SUCCESS_DELETE_ALARM(NO_CONTENT, "알림 삭제 성공"),
    ;

    private final HttpStatus status;
    private final String message;
}
