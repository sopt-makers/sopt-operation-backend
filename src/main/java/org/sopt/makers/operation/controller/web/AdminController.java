package org.sopt.makers.operation.controller.web;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.springframework.http.HttpStatus.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.ResponseDTO;
import org.sopt.makers.operation.dto.admin.request.LoginRequestDTO;
import org.sopt.makers.operation.dto.admin.request.SignUpRequestDTO;
import org.sopt.makers.operation.service.web.admin.AdminServiceImpl;
import org.sopt.makers.operation.util.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AdminController {
    private final AdminServiceImpl authService;
    private final Cookie cookie;

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        val response = authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok(ResponseDTO.success(SUCCESS_SIGN_UP.getMessage(), response));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO userLoginRequestDTO) {
        val response = authService.login(userLoginRequestDTO);
        val headers = cookie.setRefreshToken(response.refreshToken());

        return ResponseEntity.status(OK)
            .headers(headers)
            .body(ResponseDTO.success(SUCCESS_LOGIN_UP.getMessage(), response.loginResponseVO()));
    }

    @ApiOperation(value = "토큰 재발급")
    @PatchMapping("/refresh")
    public ResponseEntity<ResponseDTO> refresh(@CookieValue String refreshToken) {
        val response = authService.refresh(refreshToken);
        val headers = cookie.setRefreshToken(response.refreshToken());

        return ResponseEntity.status(OK).headers(headers)
            .body(ResponseDTO.success(SUCCESS_GET_REFRESH_TOKEN.getMessage(), response.accessToken()));
    }
}
