package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.admin.*;
import org.sopt.makers.operation.security.jwt.JwtTokenProvider;
import org.sopt.makers.operation.security.jwt.JwtTokenType;
import org.sopt.makers.operation.service.AdminServiceImpl;
import org.sopt.makers.operation.util.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AdminController {
    private final AdminServiceImpl authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Cookie cookie;

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        val response = authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_SIGN_UP.getMessage(), response));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDTO userLoginRequestDTO) {
        val response = authService.login(userLoginRequestDTO);
        val headers = cookie.setRefreshToken(response.refreshToken());

        return ResponseEntity.status(OK)
            .headers(headers)
            .body(ApiResponse.success(SUCCESS_LOGIN_UP.getMessage(), response.loginResponseVO()));
    }

    @ApiOperation(value = "토큰 재발급")
    @PatchMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@CookieValue String refreshToken) {
        val adminId = jwtTokenProvider.getId(refreshToken, JwtTokenType.REFRESH_TOKEN);
        val response = authService.refresh(adminId, refreshToken);
        val headers = cookie.setRefreshToken(response.refreshToken());

        return ResponseEntity.status(OK).headers(headers)
            .body(ApiResponse.success(SUCCESS_GET_REFRESH_TOKEN.getMessage(), response.accessToken()));
    }
}
