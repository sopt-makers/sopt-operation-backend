package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ScheduleFailureCode implements FailureCode {
	INVALID_DATE_PERM(BAD_REQUEST, "조회할 날짜 기간은 50일을 넘길 수 없습니다.");
	;

	private final HttpStatus status;
	private final String message;
}
