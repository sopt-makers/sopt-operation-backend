package org.sopt.makers.operation.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.dto.admin.*;
import org.sopt.makers.operation.security.jwt.AdminAuthentication;
import org.sopt.makers.operation.security.jwt.JwtTokenProvider;
import org.sopt.makers.operation.security.jwt.JwtTokenType;
import org.sopt.makers.operation.service.AdminServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @ApiResponses({
        @io.swagger.annotations.ApiResponse(code = 200, message = "회원가입 성공"),
        @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@RequestBody final SignUpRequestDTO signUpRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signUp(signUpRequestDTO));
    }

    @ApiOperation(value = "로그인")
    @ApiResponses({
        @io.swagger.annotations.ApiResponse(code = 200, message = "로그인 성공"),
        @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody final LoginRequestDTO userLoginRequestDTO) {
        val response = authService.login(userLoginRequestDTO);
        val refreshToken = authService.getRefreshToken(response.id());

        val cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .maxAge(Duration.ofDays(14))
                .secure(true)
                .path("/")
                .build();

        val headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);
    }

    @ApiOperation(value = "토큰 갱신")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "토큰 재발급 성공"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @PatchMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refresh(@CookieValue(name = "refreshToken") String refreshToken) {
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
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(RefreshResponseDTO.of(newAccessToken));
    }
}
