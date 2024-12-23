package org.sopt.makers.operation.web.banner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.sopt.makers.operation.dto.BaseResponse;

import org.sopt.makers.operation.web.banner.dto.request.*;
import org.springframework.http.ResponseEntity;

public interface BannerApi {
    @Operation(
            summary = "배너 상세 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배너 상세 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 배너 ID 요청"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> getBannerDetail(Long bannerId);

    @Operation(
            summary = "배너 이미지 PreSignedUrl 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PreSignedUrl 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> getIssuedPreSignedUrlForPutImage(String contentName, String imageType, String imageExtension, String contentType);

    @Operation(
            summary = "배너 생성 API",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "배너 생성 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> createBanner(BannerRequest.BannerCreate request);

    @Operation(
            summary = "배너 생성 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배너 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> updateBanner(Long bannerId, BannerRequest.BannerCreate request);
}
