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
    SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL(HttpStatus.OK, "이미지 업로드 pre signed url 조회에 성공했습니다"),
    SUCCESS_UPDATE_BANNER(HttpStatus.OK, "배너 수정에 성공했습니다"),
    SUCCESS_CREATE_BANNER(HttpStatus.CREATED, "배너 생성에 성공했습니다")
    ;

    private final HttpStatus status;
    private final String message;
}
