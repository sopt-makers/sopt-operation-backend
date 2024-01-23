package org.sopt.makers.operation.handler;

import static org.sopt.makers.operation.common.ApiResponse.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.springframework.http.HttpStatus.*;

import java.time.format.DateTimeParseException;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(AdminFailureException.class)
    public ResponseEntity<ApiResponse> authFailureException (AdminFailureException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiResponse> tokenException (TokenException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> illegalStateExceptionException (IllegalStateException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse> memberException (MemberException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<ApiResponse> lectureException (LectureException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(SubLectureException.class)
    public ResponseEntity<ApiResponse> subLectureException (SubLectureException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> dateTimeParseException () {
        return ResponseEntity.status(BAD_REQUEST).body(fail(FAULT_DATE_FORMATTER.getName()));
    }

    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<ApiResponse> AlarmException (AlarmException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

}
