package org.sopt.makers.operation.web.admin.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_SIGN_UP("회원 가입 성공"),
	SUCCESS_LOGIN_UP("로그인 성공"),
	SUCCESS_GET_REFRESH_TOKEN("토큰 재발급 성공"),
	;

	private final String content;
}
