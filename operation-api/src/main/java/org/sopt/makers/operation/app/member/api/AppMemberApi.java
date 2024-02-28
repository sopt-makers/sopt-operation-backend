package org.sopt.makers.operation.app.member.api;

import java.security.Principal;

import org.sopt.makers.operation.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NonNull;

public interface AppMemberApi {

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "앱 내 전체 출석 정보 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "전체 출석 정보 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getMemberTotalAttendance(
			@Parameter(hidden = true) @NonNull Principal principal);

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "앱 내 출석 점수 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "출석 점수 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getScore(
			@Parameter(hidden = true) @NonNull Principal principal);
}
