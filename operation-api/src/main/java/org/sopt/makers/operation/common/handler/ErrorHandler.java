package org.sopt.makers.operation.common.handler;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.exception.AdminFailureException;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.exception.AttendanceException;
import org.sopt.makers.operation.exception.DateTimeParseCustomException;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.exception.ScheduleException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AdminFailureException.class)
    public ResponseEntity<BaseResponse<?>> authFailureException(AdminFailureException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<BaseResponse<?>> tokenException(TokenException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(ScheduleException.class)
    public ResponseEntity<BaseResponse<?>> scheduleException(ScheduleException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<BaseResponse<?>> memberException(MemberException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<BaseResponse<?>> lectureException(LectureException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(SubLectureException.class)
    public ResponseEntity<BaseResponse<?>> subLectureException(SubLectureException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(DateTimeParseCustomException.class)
    public ResponseEntity<BaseResponse<?>> dateTimeParseException(DateTimeParseCustomException ex) {
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<BaseResponse<?>> alarmException(AlarmException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(AttendanceException.class)
    public ResponseEntity<BaseResponse<?>> attendanceException(AttendanceException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<BaseResponse<?>> authException(AuthException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

}
