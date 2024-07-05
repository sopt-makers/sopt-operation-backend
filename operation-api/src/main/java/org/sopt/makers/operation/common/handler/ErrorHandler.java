package org.sopt.makers.operation.common.handler;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.*;
import org.sopt.makers.operation.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(UserException.class)
    public ResponseEntity<BaseResponse<?>> userException(UserException ex) {
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

    @ExceptionHandler(ParameterDecodeCustomException.class)
    public ResponseEntity<BaseResponse<?>> ParameterDecodeException(ParameterDecodeCustomException ex) {
        log.error(ex.getMessage());
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList());

        val errorMessage = String.join(", ", errorMessages);
        log.error("[ValidationException] : {}", errorMessage);

        return ApiResponseUtil.failure(errorMessage);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            return String.format("[%s]은 필수 값입니다.", fieldError.getField());
        } else {
            return objectError.getDefaultMessage();
        }
    }

}
