package org.sopt.makers.operation.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
	SUCCESS_CREATE_LECTURE("세션 생성 성공"),
	SUCCESS_GET_LECTURE("세션 조회 성공");

	private final String message;
}
