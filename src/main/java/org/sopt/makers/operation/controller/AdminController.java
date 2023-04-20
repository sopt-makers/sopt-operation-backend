package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.admin.*;
import org.sopt.makers.operation.security.jwt.AdminAuthentication;
import org.sopt.makers.operation.security.jwt.JwtTokenProvider;
import org.sopt.makers.operation.security.jwt.JwtTokenType;
import org.sopt.makers.operation.service.AdminServiceImpl;
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

        val cookie = ResponseCookie.from("refreshToken", response.refreshToken())
            .httpOnly(true)
            .maxAge(Duration.ofDays(14))
            .secure(true)
            .path("/")
            .build();

        val headers = new HttpHeaders();
        headers.add(SET_COOKIE, cookie.toString());

        return ResponseEntity.status(OK)
            .headers(headers)
            .body(ApiResponse.success(SUCCESS_LOGIN_UP.getMessage(), response.loginResponseVO()));
    }

    @ApiOperation(value = "토큰 재발급")
    @PatchMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@CookieValue(name = "refreshToken") String refreshToken) {
        //TODO: jwtTokenProvider.getId 내부에서 validateRefreshToken 수행하고 adminId 반환 방식 제안
        val adminId = jwtTokenProvider.getId(refreshToken, JwtTokenType.REFRESH_TOKEN);
        authService.validateRefreshToken(adminId, refreshToken);

        val adminAuthentication = new AdminAuthentication(adminId, null, null);
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(adminAuthentication);
        val newAccessToken = jwtTokenProvider.generateAccessToken(adminAuthentication);
        authService.refresh(adminId, newRefreshToken);

        val cookie = ResponseCookie.from("refreshToken", newRefreshToken)
            .httpOnly(true)
            .maxAge(Duration.ofDays(14))
            .secure(true)
            .path("/")
            .build();

        val headers = new HttpHeaders();
        headers.add(SET_COOKIE, cookie.toString());

        return ResponseEntity.status(OK).headers(headers)
            .body(ApiResponse.success(SUCCESS_GET_REFRESH_TOKEN.getMessage(), newAccessToken));
    }
}
