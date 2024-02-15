package org.sopt.makers.operation.code.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Getter
public enum TokenFailureCode implements FailureCode {
	EMPTY_TOKEN(BAD_REQUEST, "빈 토큰입니다."),
	INVALID_TOKEN(BAD_REQUEST, "유효하지 않은 토큰입니다.")
	;

	private final HttpStatus status;
	private final String message;
}
