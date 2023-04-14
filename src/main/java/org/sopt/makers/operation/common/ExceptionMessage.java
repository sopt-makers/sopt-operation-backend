package org.sopt.makers.operation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
	INVALID_MEMBER("존재하지 않는 회원입니다."),
	INVALID_LECTURE("존재하지 않는 세션입니다."),
	INVALID_SUB_LECTURE("존재하지 않는 세션입니다."),
	INVALID_ATTENDANCE("존재하지 않는 출석 세션입니다."),
	NOT_STARTED_PRE_ATTENDANCE("이전의 출석체크가 시작되지 않았습니다.");

	private final String name;
}
