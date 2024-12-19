package org.sopt.makers.operation.code.success.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum BannerSuccessCode implements SuccessCode {
    SUCCESS_GET_BANNER_DETAIL(HttpStatus.OK, "배너 상세 정보 조회 성공"),
    ;

    private final HttpStatus status;
    private final String message;
}
