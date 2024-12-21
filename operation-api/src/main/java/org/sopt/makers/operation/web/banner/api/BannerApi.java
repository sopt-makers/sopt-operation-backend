package org.sopt.makers.operation.web.banner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.sopt.makers.operation.dto.BaseResponse;

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
    ResponseEntity<BaseResponse<?>> getPreSignedUrlForBanner(String bannerName, String imageTyp, String imageExtension);

}
