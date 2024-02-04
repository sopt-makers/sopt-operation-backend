package org.operation.app.schedule.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_GET_SCHEDULES("일정 리스트 조회 성공")
	;

	private final String content;
}
