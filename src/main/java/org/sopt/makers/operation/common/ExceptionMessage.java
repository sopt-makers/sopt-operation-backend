package org.sopt.makers.operation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
	INVALID_MEMBER("존재하지 않는 회원입니다."),
	INVALID_LECTURE("존재하지 않는 세션입니다."),
	INVALID_SUB_LECTURE("존재하지 않는 세션입니다."),
	INVALID_ATTENDANCE("존재하지 않는 출석 세션입니다."),
	INVALID_SUB_ATTENDANCE("존재하지 않는 N차 출석입니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN("만료된 토큰입니다."),
	INVALID_SIGNATURE("유효하지 않은 서명입니다."),
	INVALID_AUTH_REQUEST("빈 토큰입니다."),
	NOT_STARTED_PRE_ATTENDANCE("이전의 출석체크가 시작되지 않았습니다."),
	NO_SESSION("오늘 세션이 없습니다."),
	NOT_STARTED_ATTENDANCE("출석 시작 전입니다."),
	NOT_STARTED_NTH_ATTENDANCE("차 출석 시작 전입니다."),
	ENDED_ATTENDANCE("차 출석이 이미 종료되었습니다."),
	INVALID_COUNT_SESSION("세션의 개수가 올바르지 않습니다."),
	INVALID_CODE("코드가 일치하지 않아요!"),
	NOT_END_TIME_YET("세션 종료 시간이 지나지 않았습니다."),
	END_LECTURE("이미 종료된 세션입니다."),
	NO_SUB_LECTURE_EQUAL_ROUND("해당 라운드와 일치하는 출석 세션이 없습니다."),
	FAULT_DATE_FORMATTER("잘못된 날짜 형식입니다."),
	DUPLICATED_MEMBER("이미 존재하는 회원입니다."),
	INVALID_ALARM("알림이 존재하지 않습니다."),
	ALREADY_SEND_ALARM("이미 전송된 알림입니다."),
	INVALID_LINK("유효하지 않는 링크입니다."),
	FAIL_SEND_ALARM("알림 전송에 실패하였습니다."),
	FAIL_INACTIVE_USERS("비활동 유저 불러오기에 실패하였습니다.");

	private final String name;
}
