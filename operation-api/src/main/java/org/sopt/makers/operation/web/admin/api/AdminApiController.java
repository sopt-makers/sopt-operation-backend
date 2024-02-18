package org.sopt.makers.operation.web.admin.api;

import static org.sopt.makers.operation.code.success.web.AdminSuccessCode.*;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.common.util.Cookie;
import org.sopt.makers.operation.web.admin.dto.request.LoginRequest;
import org.sopt.makers.operation.web.admin.dto.request.SignUpRequest;
import org.sopt.makers.operation.web.admin.service.AdminService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@ComponentScan("operation-business")
public class AdminApiController implements AdminApi {

	private final AdminService authService;
	private final Cookie cookie;

	@Override
	@PostMapping("/signup")
	public ResponseEntity<BaseResponse<?>> signup(SignUpRequest signUpRequestDTO) {
		val response = authService.signUp(signUpRequestDTO);
		return ApiResponseUtil.success(SUCCESS_SIGN_UP, response);
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<?>> login(LoginRequest userLoginRequestDTO) {
		val response = authService.login(userLoginRequestDTO);
		val headers = cookie.setRefreshToken(response.refreshToken());
		return ApiResponseUtil.success(SUCCESS_LOGIN_UP, headers, response.loginResponseVO());
	}

	@Override
	@PatchMapping("/refresh")
	public ResponseEntity<BaseResponse<?>> refresh(String refreshToken) {
		val response = authService.refresh(refreshToken);
		return ApiResponseUtil.success(SUCCESS_GET_REFRESH_TOKEN, response);
	}
}
