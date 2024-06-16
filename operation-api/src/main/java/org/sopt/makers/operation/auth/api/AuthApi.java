package org.sopt.makers.operation.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.makers.operation.auth.dto.request.AccessTokenRequest;
import org.sopt.makers.operation.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "SSO 기반 인증 관련 API")
public interface AuthApi {

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "플랫폼 인가코드 반환 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "플랫폼 인가코드 반환 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                                    1. 쿼리 파라미터 중 데이터가 들어오지 않았습니다.\n
                                    2. 유효하지 않은 social type 입니다.\n
                                    3. 유효하지 않은 id token 입니다.\n
                                    4. 유효하지 않은 social code 입니다.
                                    """
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = """
                                    1. 등록되지 않은 팀입니다.\n
                                    2. 등록된 소셜 정보가 없습니다.
                                    """
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> authorize(
            @RequestParam String type,
            @RequestParam String code,
            @RequestParam String clientId,
            @RequestParam String redirectUri
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "인증 토큰 발급 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰 발급 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                                    1. grantType 데이터가 들어오지 않았습니다.\n
                                    2. 유효하지 않은 grantType 입니다.\n
                                    3. 플랫폼 인가코드가 들어오지 않았습니다.\n
                                    4. 이미 사용한 플랫폼 인가코드입니다.\n
                                    5. 리프레쉬 토큰이 들어오지 않았습니다.
                                    """
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = """
                                    1. 만료된 플랫폼 인가 코드입니다.\n
                                    2. 만료된 리프레쉬 토큰입니다.
                                    """
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> token(AccessTokenRequest accessTokenRequest);
}
