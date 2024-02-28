package org.sopt.makers.operation.code.success.web;

import static org.springframework.http.HttpStatus.*;

import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberSuccessCode implements SuccessCode {
	SUCCESS_GET_MEMBERS(OK, "유저 리스트 조회 성공"),
	;

	private final HttpStatus status;
	private final String message;
}
