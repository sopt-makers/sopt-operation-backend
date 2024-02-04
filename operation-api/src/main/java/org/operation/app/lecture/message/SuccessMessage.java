package org.operation.app.lecture.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_SINGLE_GET_LECTURE("세션 조회 성공"),
	SUCCESS_GET_LECTURE_ROUND("출석 차수 조회 성공"),
	;

	private final String content;
}
