package org.sopt.makers.operation.app.member.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_TOTAL_ATTENDANCE("전체 출석 정보 조회 성공"),
	SUCCESS_GET_ATTENDANCE_SCORE("출석 점수 조회 성공"),
	;

	private final String content;
}
