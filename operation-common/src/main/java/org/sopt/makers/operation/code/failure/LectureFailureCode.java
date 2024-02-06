package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LectureFailureCode implements FailureCode {
	INVALID_DATE_PATTERN(BAD_REQUEST, "유효하지 않은 날짜 형식입니다."),
	INVALID_SUB_LECTURE(NOT_FOUND, "존재하지 않는 세션입니다."),
	INVALID_CODE(BAD_REQUEST, "코드가 일치하지 않아요!"),
	NOT_STARTED_NTH_ATTENDANCE(BAD_REQUEST, "차 출석 시작 전입니다."),
	ENDED_ATTENDANCE(BAD_REQUEST, "차 출석이 이미 종료되었습니다."),
	INVALID_LECTURE(NOT_FOUND, "존재하지 않는 세션입니다."),
	NOT_END_TIME_YET(BAD_REQUEST, "세션 종료 시간이 지나지 않았습니다."),
	NO_SUB_LECTURE_EQUAL_ROUND(NOT_FOUND, "해당 라운드와 일치하는 출석 세션이 없습니다."),
	END_LECTURE(BAD_REQUEST, "이미 종료된 세션입니다."),
	NOT_STARTED_PRE_ATTENDANCE(BAD_REQUEST, "이전의 출석체크가 시작되지 않았습니다."),
	INVALID_COUNT_SESSION(BAD_REQUEST, "세션의 개수가 올바르지 않습니다."),
	NO_SESSION(NOT_FOUND, "오늘 세션이 없습니다."),
	NOT_STARTED_ATTENDANCE(BAD_REQUEST, "출석 시작 전입니다."),
	;

	private final HttpStatus status;
	private final String message;
}
