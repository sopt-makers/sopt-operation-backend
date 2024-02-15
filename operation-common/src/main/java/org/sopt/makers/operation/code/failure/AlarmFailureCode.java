package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmFailureCode implements FailureCode {
	FAIL_SEND_ALARM(BAD_REQUEST, "알림 전송에 실패하였습니다."),
	SENT_ALARM(BAD_REQUEST, "전송된 알림입니다."),
	INVALID_ALARM(NOT_FOUND, "알림이 존재하지 않습니다."),
	FAIL_INACTIVE_USERS(BAD_REQUEST, "비활동 유저 불러오기에 실패하였습니다."),
	;

	private final HttpStatus status;
	private final String message;
}
