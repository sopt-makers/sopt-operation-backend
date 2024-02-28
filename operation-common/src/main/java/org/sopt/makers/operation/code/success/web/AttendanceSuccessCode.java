package org.sopt.makers.operation.code.success.web;

import static org.springframework.http.HttpStatus.*;

import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AttendanceSuccessCode implements SuccessCode {
	SUCCESS_UPDATE_ATTENDANCE_STATUS(OK, "출석 상태 변경 성공"),
	SUCCESS_GET_MEMBER_ATTENDANCE(OK, "회원 출석 정보 조회 성공"),
	SUCCESS_UPDATE_MEMBER_SCORE(OK, "회원 출석 점수 갱신 성공"),
	SUCCESS_GET_ATTENDANCES(OK, "출석 리스트 조회 성공"),
	;

	private final HttpStatus status;
	private final String message;
}
