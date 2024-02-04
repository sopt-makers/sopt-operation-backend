package org.operation.web.lecture.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	SUCCESS_CREATE_LECTURE("세션 생성 성공"),
	SUCCESS_GET_LECTURES("세션 리스트 조회 성공"),
	SUCCESS_GET_LECTURE("세션 상세 조회 성공"),
	SUCCESS_START_ATTENDANCE("출석 시작 성공"),
	SUCCESS_GET_MEMBERS("유저 리스트 조회 성공"),
	SUCCESS_DELETE_LECTURE("세션 삭제 성공"),
	SUCCESS_UPDATE_MEMBER_SCORE("회원 출석 점수 갱신 성공"),
	;

	private final String content;
}
