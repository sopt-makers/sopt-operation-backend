package org.sopt.makers.operation.domain.member.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
	INVALID_MEMBER("존재하지 않는 회원입니다.")
	;

	private final String content;
}
