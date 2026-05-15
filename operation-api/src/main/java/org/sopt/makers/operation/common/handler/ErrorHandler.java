package org.sopt.makers.operation.common.handler;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.sopt.makers.operation.code.failure.ApiKeyFailureCode;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.*;
import org.sopt.makers.operation.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<BaseResponse<?>> permissionException(PermissionException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(BannerException.class)
    public ResponseEntity<BaseResponse<?>> bannerException(BannerException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(ExternalException.class)
    public ResponseEntity<BaseResponse<?>> externalException(ExternalException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> validationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList());

        val errorMessage = String.join(", ", errorMessages);
        log.error("[ValidationException] : {}", errorMessage);

        return ApiResponseUtil.failure(errorMessage);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            return String.format("[%s]은 필수 값입니다.", ((FieldError) objectError).getField());
        } else {
            return objectError.getDefaultMessage();
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationFailure(MissingServletRequestParameterException ex) {
        log.error("[Missing Parameter Exception] : {}", ex.getMessage());
        return ApiResponseUtil.failure(ex.getMessage());
    }


    @ExceptionHandler(ApiKeyException.class)
    public ResponseEntity<BaseResponse<?>> apiKeyException(ApiKeyException ex) {
        log.error(ex.getMessage());
        return ApiResponseUtil.failure(ex.getFailureCode());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponse<?>> missingHeaderException(MissingRequestHeaderException ex) {
        log.error("[Missing Header Exception] : {}", ex.getMessage());

        if ("api-key".equals(ex.getHeaderName())) {
            return ApiResponseUtil.failure(ApiKeyFailureCode.MISSING_API_KEY);
        }

        return ApiResponseUtil.failure("필수 헤더가 누락되었습니다: " + ex.getHeaderName());
    }

    // @ModelAttribute 폼 바인딩/타입 변환 실패 (예: LocalDate 파싱 실패, MultipartFile 매핑 실패)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<?>> bindException(BindException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(this::getBindErrorMessage)
                .collect(Collectors.toList());

        val errorMessage = errorMessages.isEmpty()
                ? ex.getMessage()
                : String.join(", ", errorMessages);
        log.error("[BindException] : {}", errorMessage, ex);

        return ApiResponseUtil.failure("요청 값 바인딩 실패: " + errorMessage);
    }

    private String getBindErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError fieldError) {
            return String.format("[%s] 값이 올바르지 않습니다. (rejected=%s, reason=%s)",
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage());
        }
        return objectError.getDefaultMessage();
    }

    // 파일 크기 초과 (max-file-size / max-request-size)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse<?>> maxUploadSizeException(MaxUploadSizeExceededException ex) {
        log.error("[MaxUploadSizeExceededException] : {}", ex.getMessage(), ex);
        return ApiResponseUtil.failure("업로드 파일 크기가 허용 한도를 초과했습니다: " + ex.getMessage());
    }

    // multipart 파싱 자체 실패 (boundary 오류, 손상된 body 등)
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<BaseResponse<?>> multipartException(MultipartException ex) {
        log.error("[MultipartException] : {}", ex.getMessage(), ex);
        return ApiResponseUtil.failure("multipart 요청 파싱 실패: " + ex.getMessage());
    }

    // @RequestPart 필수 파트 누락
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<BaseResponse<?>> missingPartException(MissingServletRequestPartException ex) {
        log.error("[MissingServletRequestPartException] : {}", ex.getMessage());
        return ApiResponseUtil.failure("필수 multipart 파트가 누락되었습니다: " + ex.getRequestPartName());
    }

    // JSON 바디 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> notReadableException(HttpMessageNotReadableException ex) {
        log.error("[HttpMessageNotReadableException] : {}", ex.getMessage(), ex);
        return ApiResponseUtil.failure("요청 바디를 읽을 수 없습니다: " + ex.getMostSpecificCause().getMessage());
    }

    // 위에서 잡지 못한 모든 예외에 대한 fallback — 람다에서 body 비어버리는 문제 방지
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> fallbackException(Exception ex) {
        log.error("[UnhandledException] {} : {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ApiResponseUtil.failure(ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }
}
