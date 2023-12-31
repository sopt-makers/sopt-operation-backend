package org.sopt.makers.operation.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

	/** auth **/
	SUCCESS_SIGN_UP("회원 가입 성공"),
	SUCCESS_LOGIN_UP("로그인 성공"),
	SUCCESS_GET_REFRESH_TOKEN("토큰 재발급 성공"),

	/** app **/
	SUCCESS_SINGLE_GET_LECTURE("세션 조회 성공"),
	SUCCESS_TOTAL_ATTENDANCE("전체 출석정보 조회 성공"),
	SUCCESS_GET_ATTENDANCE_SCORE("출석 점수 조회 성공"),
	SUCCESS_GET_LECTURE_ROUND("출석 차수 조회 성공"),
	SUCCESS_ATTEND("출석 성공"),
	SUCCESS_CREATE_MEMBER("회원 등록 성공"),

	/** attendance **/
	SUCCESS_UPDATE_ATTENDANCE_STATUS("출석 상태 변경 성공"),
	SUCCESS_GET_MEMBER_ATTENDANCE("회원 출석 정보 조회 성공"),
	SUCCESS_UPDATE_MEMBER_SCORE("회원 출석 점수 갱신 성공"),
	SUCCESS_GET_ATTENDANCES("출석 리스트 조회 성공"),

	/** lecture **/
	SUCCESS_CREATE_LECTURE("세션 생성 성공"),
	SUCCESS_GET_LECTURES("세션 리스트 조회 성공"),
	SUCCESS_GET_LECTURE("세션 상세 조회 성공"),
	SUCCESS_START_ATTENDANCE("출석 시작 성공"),
	SUCCESS_GET_MEMBERS("유저 리스트 조회 성공"),
	SUCCESS_DELETE_LECTURE("세션 삭제 성공"),

	/** alarm **/
	SUCCESS_SEND_ALARM("알림 전송 성공"),
	SUCCESS_CREATE_ALARM("알림 생성 성공"),
	SUCCESS_GET_ALARMS("알림 리스트 조회 성공"),
	SUCCESS_GET_ALARM("알림 상세 조회 성공"),
	SUCCESS_DELETE_ALARM("알림 삭제 성공"),

	/** schedule **/
	SUCCESS_GET_SCHEDULES("일정 리스트 조회 성공")
	;

	private final String message;
}
