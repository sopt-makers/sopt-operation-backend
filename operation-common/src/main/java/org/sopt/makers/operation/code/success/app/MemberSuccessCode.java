package org.sopt.makers.operation.code.success.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements SuccessCode {
	SUCCESS_GET_TOTAL_ATTENDANCE(OK,"전체 출석정보 조회 성공"),
	SUCCESS_GET_ATTENDANCE_SCORE(OK,"출석 점수 조회 성공");

	private final HttpStatus status;
	private final String message;
}
