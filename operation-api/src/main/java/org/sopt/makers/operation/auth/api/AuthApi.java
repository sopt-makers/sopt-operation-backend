package org.sopt.makers.operation.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.sopt.makers.operation.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

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
                                    쿼리 파라미터 중 데이터가 들어오지 않았습니다.
                                    유효하지 않은 social type 입니다.
                                    유효하지 않은 id token 입니다.
                                    유효하지 않은 social code 입니다.
                                    """
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = """
                                    등록되지 않은 팀입니다.
                                    등록된 소셜 정보가 없습니다.
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
}
