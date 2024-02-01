package org.sopt.makers.operation.handler;

import static org.sopt.makers.operation.dto.ResponseDTO.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.springframework.http.HttpStatus.*;

import java.time.format.DateTimeParseException;

import org.sopt.makers.operation.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(AdminFailureException.class)
    public ResponseEntity<ResponseDTO> authFailureException (AdminFailureException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ResponseDTO> tokenException (TokenException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDTO> illegalStateExceptionException (IllegalStateException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ResponseDTO> memberException (MemberException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(LectureException.class)
    public ResponseEntity<ResponseDTO> lectureException (LectureException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(SubLectureException.class)
    public ResponseEntity<ResponseDTO> subLectureException (SubLectureException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(fail(ex.getMessage()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ResponseDTO> dateTimeParseException () {
        return ResponseEntity.status(BAD_REQUEST).body(fail(FAULT_DATE_FORMATTER.getName()));
    }

    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<ResponseDTO> AlarmException (AlarmException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(fail(ex.getMessage()));
    }

}
