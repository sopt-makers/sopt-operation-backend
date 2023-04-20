package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.admin.*;
import org.sopt.makers.operation.entity.Admin;

public interface AdminService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);
    LoginResponseDTO login(LoginRequestDTO userLoginRequestDTO);
    void validateRefreshToken(Long adminId, String requestRefreshToken);
    RefreshResponseDTO refresh(String refreshToken);
}
