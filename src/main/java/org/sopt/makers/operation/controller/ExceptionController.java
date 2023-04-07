package org.sopt.makers.operation.controller;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.exception.AdminFailureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.exception.TokenException;
import org.sopt.makers.operation.exception.LectureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(AdminFailureException.class)
    public ResponseEntity<String> authFailureException (AdminFailureException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> TokenException (TokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse> MemberException (MemberException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<ApiResponse> LectureException (LectureException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ex.getMessage()));
    }

}