package org.sopt.makers.operation.code.failure.subLecture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum subLectureFailureCode implements FailureCode {
    INVALID_SUB_ATTENDANCE(BAD_REQUEST, "존재하지 않는 N차 출석입니다."),
    NO_SUB_LECTURE_EQUAL_ROUND(BAD_REQUEST,"해당 라운드와 일치하는 출석 세션이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
