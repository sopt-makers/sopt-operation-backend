package org.sopt.makers.operation.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NonNull;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.user.dto.request.UserModifyRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

public interface UserApi {

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "본인 유저 정보 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "본인에 대한 유저 정보 조회가 성공한 경우"
					),
					@ApiResponse(
							responseCode = "400",
							description = "요청이 잘못된 경우"
					),
					@ApiResponse(
							responseCode = "404",
							description = "해당 토큰에 담긴 유저 정보에 해당하는 데이터가 존재하지 않은 경우"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류가 발생한 경우"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> getUserInfoSelf(
			@Parameter(hidden = true) @NonNull Principal principal);

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "복수 유저 정보 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "의도했던 유저 정보 조회가 성공한 경우"
					),
					@ApiResponse(
							responseCode = "400",
							description = "요청이 잘못된 경우"
					),
					@ApiResponse(
							responseCode = "404",
							description = "쿼리 파라미터에 담긴 유저 ID들 중 존재하지 않는 유저에 대한 ID가 존재하는 경우"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류가 발생한 경우"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> getUserInfoOf(
			@Parameter(hidden = true) @NonNull Principal principal,
			@Parameter String targetUserIds);

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "유저 정보 수정 API",
			responses = {
					@ApiResponse(
							responseCode = "204",
							description = "유저 정보 수정 성공한 경우"
					),
					@ApiResponse(
							responseCode = "400",
							description = """
									요청이 잘못된 경우<br/>
									1. Request DTO 내 Validation에 실패한 경우(NotNull/NotEmpty)
									2. 임원진(Position != MEMBER)이 Team 변경을 요청한 경우
									"""
					),
					@ApiResponse(
							responseCode = "404",
							description = "해당 토큰에 담긴 유저 정보에 해당하는 데이터가 존재하지 않은 경우"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류가 발생한 경우"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> modifyUserInfoOf(
			@PathVariable long userId,
			@RequestBody UserModifyRequest userModifyRequest);
}
