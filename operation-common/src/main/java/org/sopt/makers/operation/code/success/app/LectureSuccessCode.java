package org.sopt.makers.operation.code.success.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@RequiredArgsConstructor
public enum LectureSuccessCode implements SuccessCode {
	SUCCESS_SINGLE_GET_LECTURE(OK,"세션 조회 성공"),
	SUCCESS_GET_LECTURE_ROUND(OK,"출석 차수 조회 성공");

	private final HttpStatus status;
	private final String message;
}
