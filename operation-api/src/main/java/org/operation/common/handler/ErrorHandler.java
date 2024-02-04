package org.operation.common.handler;

import static org.springframework.http.HttpStatus.*;

import java.time.format.DateTimeParseException;

import org.operation.common.dto.BaseResponse;
import org.operation.common.exception.MemberException;
import org.operation.common.util.ApiResponseUtil;
import org.operation.common.exception.AdminFailureException;
import org.operation.common.exception.TokenException;
import org.operation.common.exception.AlarmException;
import org.operation.common.exception.LectureException;
import org.operation.common.exception.SubLectureException;
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
        return ApiResponseUtil.failure(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<BaseResponse<?>> tokenException(TokenException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse<?>> illegalStateExceptionException(IllegalStateException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<BaseResponse<?>> memberException(MemberException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<BaseResponse<?>> lectureException(LectureException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SubLectureException.class)
    public ResponseEntity<BaseResponse<?>> subLectureException(SubLectureException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<BaseResponse<?>> dateTimeParseException(DateTimeParseException ex) {
        return ApiResponseUtil.failure(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<BaseResponse<?>> alarmException(AlarmException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(BAD_REQUEST, ex.getMessage());
    }

}
