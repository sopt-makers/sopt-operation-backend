package org.sopt.makers.operation.code.success.web;

import static org.springframework.http.HttpStatus.*;

import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LectureSuccessCode implements SuccessCode {
	SUCCESS_CREATE_LECTURE(CREATED, "세션 생성 성공"),
	SUCCESS_GET_LECTURES(OK, "세션 리스트 조회 성공"),
	SUCCESS_GET_LECTURE(OK, "세션 상세 조회 성공"),
	SUCCESS_START_ATTENDANCE(CREATED, "출석 시작 성공"),
	SUCCESS_GET_MEMBERS(OK, "유저 리스트 조회 성공"),
	SUCCESS_DELETE_LECTURE(OK, "세션 삭제 성공"),
	SUCCESS_UPDATE_MEMBER_SCORE(OK, "회원 출석 점수 갱신 성공"),
	;

	private final HttpStatus status;
	private final String message;
}
