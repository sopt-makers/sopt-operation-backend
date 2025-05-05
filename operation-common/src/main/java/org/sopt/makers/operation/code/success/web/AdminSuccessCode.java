package org.sopt.makers.operation.code.success.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@RequiredArgsConstructor
public enum AdminSuccessCode implements SuccessCode {
	SUCCESS_ATTEND(OK, "출석 성공"),
	SUCCESS_SIGN_UP(OK,"회원 가입 성공"),
	SUCCESS_LOGIN_UP(OK,"로그인 성공"),
	SUCCESS_GET_REFRESH_TOKEN(OK,"토큰 재발급 성공"),
	SUCCESS_CHANGE_PASSWORD(OK, "비밀번호 변경 성공");
	private final HttpStatus status;
	private final String message;
}
