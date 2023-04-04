package org.sopt.makers.operation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
	INVALID_MEMBER("존재하지 않는 회원입니다."),
	INVALID_LECTURE("존재하지 않는 세션입니다.");

	private final String name;
}
