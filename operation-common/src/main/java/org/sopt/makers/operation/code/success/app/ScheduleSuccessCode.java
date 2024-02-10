package org.sopt.makers.operation.code.success.app;

import static org.springframework.http.HttpStatus.*;

import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleSuccessCode implements SuccessCode {
	SUCCESS_GET_SCHEDULES(OK, "일정 리스트 조회 성공"),
	;

	private final HttpStatus status;
	private final String message;
}
