package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AttendanceFailureCode implements FailureCode {
	INVALID_ATTENDANCE(NOT_FOUND, "존재하지 않는 출석 세션입니다."),
	INVALID_SUB_ATTENDANCE(NOT_FOUND, "존재하지 않는 N차 출석입니다."),
	;

	private final HttpStatus status;
	private final String message;
}
