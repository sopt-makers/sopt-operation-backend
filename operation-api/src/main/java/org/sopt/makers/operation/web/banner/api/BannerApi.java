package org.sopt.makers.operation.web.banner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.sopt.makers.operation.dto.BaseResponse;

import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;
import org.springframework.http.MediaType;
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
            summary = "배너 목록 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배너 목록 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청<br/><br/>1. 존재하지 않는 Parameter Key<br/>2. 존재하지 않는 Parameter Value"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> getBanners(
            String status,
            String sort,
            Integer page,
            Integer limit);

    @Operation(
            summary = "배너 삭제 API",
            description = "배너를 삭제합니다. 게시 종료(DONE) 상태의 배너는 삭제할 수 없습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배너 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청<br/><br/>1. 존재하지 않는 Parameter 값<br/>2. 게시 종료된 배너 삭제 시도"
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
  ResponseEntity<BaseResponse<?>> deleteBanner(Long bannerId);

  @Operation(
      summary = "게시 중인 외부 배너 리스트 조회  API (PC/Mobile 동시 조회)",
          description="추가한 배너의 PC/Mobile 접근 URL과 게시 기간을 한 번에 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "게시 중인 외부 배너 리스트 조회 성공"
          ),
              @ApiResponse(
                      responseCode = "400",
                      description = "잘못된 요청<br/><br/>1. 유효하지 않은 API 키 <br/>2. 잘못된 파라미터 값",
                      content = @Content(
                              mediaType = "application/json",
                              examples = {
                                      @ExampleObject(
                                              name = "유효하지 않은 API 키",
                                              value = """
                        {
                          "success": false,
                          "message": "API 키가 유효하지 않습니다."
                        }
                        """
                                      ),
                                      @ExampleObject(
                                              name = "잘못된 파라미터",
                                              value = """
                        {
                          "success": false,
                          "message": "지원하지 않는 장소 유형입니다."
                        }
                        """
                                      )
                              }
                      )
              ),
          @ApiResponse(
              responseCode = "500",
              description = "서버 내부 오류"
          )
      },
          parameters = {

                  @Parameter(
                          name = "location",
                          description = "배너 게시 위치",
                          required = true,
                          example = "pg_community",
                          schema = @Schema(type = "string", allowableValues = {"pg_community", "cr_main", "cr_feed"})
                  ),
                  @Parameter(
                          name = "api-key",
                          description = "API 인증 키 (헤더로 전송)",
                          required = true,
                          in = ParameterIn.HEADER,
                          example = "your-api-key-here",
                          schema = @Schema(type = "string")
                  )
          }
  )
  ResponseEntity<BaseResponse<?>> getExternalBanners(String location,String apiKey);

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
            },
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = BannerRequest.BannerCreateOrModify.class)
                    )
            )
    )
    ResponseEntity<BaseResponse<?>> createBanner(BannerRequest.BannerCreateOrModify request);

    @Operation(
            summary = "배너 수정 API",
            description = "배너 정보를 수정합니다. 게시 종료(DONE) 상태의 배너는 수정할 수 없습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배너 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청<br/><br/>1. 존재하지 않는 Parameter 값<br/>2. 게시 종료된 배너 수정 시도"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            },
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = BannerRequest.BannerCreateOrModify.class)
                    )
            )
    )
    ResponseEntity<BaseResponse<?>> updateBanner(Long bannerId, BannerRequest.BannerCreateOrModify request);


}
