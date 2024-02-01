package org.operation.app.attendance.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_GET_ATTENDANCE("출석 성공"),
	;

	private final String content;
}
