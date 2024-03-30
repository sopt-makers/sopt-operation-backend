package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberFailureCode implements FailureCode {
	INVALID_MEMBER(NOT_FOUND, "존재하지 않는 회원입니다.")
	;

	private final HttpStatus status;
	private final String message;
}
