package org.sopt.makers.operation.domain.alarm.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
	FAIL_SEND_ALARM("알림 전송에 실패하였습니다."),
	INVALID_ALARM("알림이 존재하지 않습니다."),
	FAIL_INACTIVE_USERS("비활동 유저 불러오기에 실패하였습니다."),
	;

	private final String content;
}
