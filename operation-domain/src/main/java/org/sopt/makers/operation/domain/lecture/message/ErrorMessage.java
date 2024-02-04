package org.sopt.makers.operation.domain.lecture.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
	INVALID_SUB_LECTURE("존재하지 않는 세션입니다."),
	INVALID_CODE("코드가 일치하지 않아요!"),
	NOT_STARTED_NTH_ATTENDANCE("차 출석 시작 전입니다."),
	ENDED_ATTENDANCE("차 출석이 이미 종료되었습니다."),
	INVALID_LECTURE("존재하지 않는 세션입니다."),
	NOT_END_TIME_YET("세션 종료 시간이 지나지 않았습니다."),
	NO_SUB_LECTURE_EQUAL_ROUND("해당 라운드와 일치하는 출석 세션이 없습니다."),
	END_LECTURE("이미 종료된 세션입니다."),
	NOT_STARTED_PRE_ATTENDANCE("이전의 출석체크가 시작되지 않았습니다."),
	INVALID_COUNT_SESSION("세션의 개수가 올바르지 않습니다."),
	NO_SESSION("오늘 세션이 없습니다."),
	NOT_STARTED_ATTENDANCE("출석 시작 전입니다."),
	;

	private final String content;
}
