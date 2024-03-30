package org.sopt.makers.operation.code.success.web;

import static org.springframework.http.HttpStatus.*;

import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmSuccessCode implements SuccessCode {
	SUCCESS_SEND_ALARM(OK, "알림 전송 성공"),
	SUCCESS_CREATE_ALARM(CREATED, "알림 생성 성공"),
	SUCCESS_GET_ALARMS(OK, "알림 리스트 조회 성공"),
	SUCCESS_GET_ALARM(OK, "알림 상세 조회 성공"),
	SUCCESS_DELETE_ALARM(OK, "알림 삭제 성공"),
	;

	private final HttpStatus status;
	private final String message;
}
