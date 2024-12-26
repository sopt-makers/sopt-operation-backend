package org.sopt.makers.operation.code.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Getter
public enum BannerFailureCode implements FailureCode {
    INVALID_BANNER_PERIOD(BAD_REQUEST, "배너 게시 기간이 올바르지 않습니다."),
    INVALID_IMAGE_EXTENSION(BAD_REQUEST, "지원하지 않는 배너 이미지 형식입니다."),
    INVALID_IMAGE_TYPE(BAD_REQUEST, "지원하지 않는 이미지 타입입니다."),
    NOT_FOUND_STATUS(NOT_FOUND, "존재하지 않는 게시 상태입니다."),
    NOT_FOUND_LOCATION(NOT_FOUND, "존재하지 않는 게시 위치입니다."),
    NOT_FOUND_CONTENT_TYPE(NOT_FOUND, "존재하지 않는 게시 유형입니다."),
    NOT_FOUNT_BANNER(NOT_FOUND, "존재하지 않는 배너입니다."),
    NOT_SUPPORTED_PLATFORM_TYPE(NOT_FOUND, "지원하지 않는 플랫폼 유형입니다."),
    NOT_FOUND_BANNER(NOT_FOUND, "존재하지 않는 배너입니다."),
    NOT_FOUND_BANNER_IMAGE(NOT_FOUND, "존재하지 않는 배너 이미지입니다.")

    ;

    private final HttpStatus status;
    private final String message;
}
