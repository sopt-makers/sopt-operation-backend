package org.sopt.makers.operation.web.admin.api;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.web.admin.dto.request.LoginRequest;
import org.sopt.makers.operation.web.admin.dto.request.PasswordChangeRequest;
import org.sopt.makers.operation.web.admin.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

public interface AdminApi {

	@Operation(
			summary = "회원가입 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "회원가입 성공"
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
	ResponseEntity<BaseResponse<?>> signup(@RequestBody SignUpRequest signUpRequestDTO);


	@Operation(
			summary = "로그인 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "로그인 성공"
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
	ResponseEntity<BaseResponse<?>> login(@RequestBody LoginRequest userLoginRequestDTO);


	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "앱 내 일정 리스트 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "일정 리스트 조회 성공"
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
	ResponseEntity<BaseResponse<?>> refresh(@CookieValue String refreshToken);




	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "비밀번호 변경 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "비밀번호 변경 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "401",
							description = "인증 실패"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> changePassword(@RequestBody PasswordChangeRequest request);



}
