package org.sopt.makers.operation.web.admin.api;

import static org.sopt.makers.operation.code.success.web.AdminSuccessCode.*;

import org.sopt.makers.operation.authentication.AdminAuthentication;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.common.util.Cookie;
import org.sopt.makers.operation.web.admin.dto.request.LoginRequest;
import org.sopt.makers.operation.web.admin.dto.request.PasswordChangeRequest;
import org.sopt.makers.operation.web.admin.dto.request.SignUpRequest;
import org.sopt.makers.operation.web.admin.service.AdminService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

	@PatchMapping("/password")
	@Override
	public ResponseEntity<BaseResponse<?>> changePassword(
			@RequestBody PasswordChangeRequest request
	) {
		val authentication = SecurityContextHolder.getContext().getAuthentication();
		Long adminId = Long.parseLong(authentication.getPrincipal().toString());

		authService.changePassword(adminId, request);
		return ApiResponseUtil.success(SUCCESS_CHANGE_PASSWORD);
	}




}
