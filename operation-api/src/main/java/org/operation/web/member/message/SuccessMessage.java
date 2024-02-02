package org.operation.web.member.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_GET_MEMBERS("유저 리스트 조회 성공"),
	;

	private final String content;
}
