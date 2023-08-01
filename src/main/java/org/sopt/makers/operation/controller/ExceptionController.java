package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ApiResponse.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.springframework.http.HttpStatus.*;

import java.time.format.DateTimeParseException;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(AdminFailureException.class)
    public ResponseEntity<ApiResponse> authFailureException (AdminFailureException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiResponse> tokenException (TokenException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> illegalStateExceptionException (IllegalStateException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse> memberException (MemberException ex) {
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<ApiResponse> lectureException (LectureException ex) {
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(SubLectureException.class)
    public ResponseEntity<ApiResponse> subLectureException (SubLectureException ex) {
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> dateTimeParseException () {
        return ResponseEntity.status(BAD_REQUEST).body(fail(FAULT_DATE_FORMATTER.getName()));
    }

}
