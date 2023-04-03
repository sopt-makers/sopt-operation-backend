package org.sopt.makers.operation.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.dto.admin.LoginRequestDTO;
import org.sopt.makers.operation.dto.admin.LoginResponseDTO;
import org.sopt.makers.operation.dto.admin.SignUpRequestDTO;
import org.sopt.makers.operation.dto.admin.SignUpResponseDTO;
import org.sopt.makers.operation.service.AdminServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AdminController {
    private final AdminServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@RequestBody final SignUpRequestDTO signUpRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signUp(signUpRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody final LoginRequestDTO userLoginRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(userLoginRequestDTO));
    }
}
