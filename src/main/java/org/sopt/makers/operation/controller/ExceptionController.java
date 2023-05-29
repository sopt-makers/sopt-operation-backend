package org.sopt.makers.operation.controller;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(AdminFailureException.class)
    public ResponseEntity<ApiResponse> authFailureException (AdminFailureException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiResponse> TokenException (TokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> IllegalStateExceptionException (IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse> MemberException (MemberException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<ApiResponse> LectureException (LectureException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(SubLectureException.class)
    public ResponseEntity<ApiResponse> SubLectureException (SubLectureException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(ex.getMessage()));
    }

}
