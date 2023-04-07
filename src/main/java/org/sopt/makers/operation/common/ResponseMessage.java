package org.sopt.makers.operation.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
	SUCCESS_CREATE_LECTURE("세션 생성 성공"),
	SUCCESS_SINGLE_GET_LECTURE("세션 조회 성공"),
	SUCCESS_GET_LECTURES("세션 리스트 조회 성공"),
	SUCCESS_GET_LECTURE("세션 상세 조회 성공"),
	SUCCESS_START_ATTENDANCE("출석 시작 성공"),
	SUCCESS_UPDATE_ATTENDANCE_STATUS("출석 상태 변경 성공"),
	SUCCESS_GET_MEMBER_ATTENDANCE("회원 출석 정보 조회 성공");

	private final String message;
}
