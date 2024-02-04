package org.sopt.makers.operation.web.attendnace.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_UPDATE_ATTENDANCE_STATUS("출석 상태 변경 성공"),
	SUCCESS_GET_MEMBER_ATTENDANCE("회원 출석 정보 조회 성공"),
	SUCCESS_UPDATE_MEMBER_SCORE("회원 출석 점수 갱신 성공"),
	SUCCESS_GET_ATTENDANCES("출석 리스트 조회 성공"),
	;

	private final String content;
}
