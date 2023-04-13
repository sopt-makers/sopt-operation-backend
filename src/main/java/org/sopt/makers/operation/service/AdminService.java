package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.admin.LoginRequestDTO;
import org.sopt.makers.operation.dto.admin.LoginResponseDTO;
import org.sopt.makers.operation.dto.admin.SignUpRequestDTO;
import org.sopt.makers.operation.dto.admin.SignUpResponseDTO;

public interface AdminService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);
    LoginResponseDTO login(LoginRequestDTO userLoginRequestDTO);
    void confirmAdmin(Long adminId);
    String getRefreshToken(Long adminId);
}
