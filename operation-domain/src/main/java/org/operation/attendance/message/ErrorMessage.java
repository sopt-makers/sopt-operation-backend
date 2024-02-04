package org.operation.attendance.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
	INVALID_ATTENDANCE("존재하지 않는 출석 세션입니다."),
	INVALID_SUB_ATTENDANCE("존재하지 않는 N차 출석입니다."),
	;

	private final String content;
}
