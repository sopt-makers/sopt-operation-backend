package org.sopt.makers.operation.web.alarm.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_SEND_ALARM("알림 전송 성공"),
	SUCCESS_CREATE_ALARM("알림 생성 성공"),
	SUCCESS_GET_ALARMS("알림 리스트 조회 성공"),
	SUCCESS_GET_ALARM("알림 상세 조회 성공"),
	SUCCESS_DELETE_ALARM("알림 삭제 성공"),
	;

	private final String content;
}
