package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LectureFailureCode implements FailureCode {
	INVALID_DATE_PATTERN(BAD_REQUEST, "유효하지 않은 날짜 형식입니다."),
	;

	private final HttpStatus status;
	private final String message;
}
