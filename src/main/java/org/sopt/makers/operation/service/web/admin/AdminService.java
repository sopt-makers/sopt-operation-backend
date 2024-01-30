package org.sopt.makers.operation.service.web.admin;

import org.sopt.makers.operation.dto.admin.request.LoginRequestDTO;
import org.sopt.makers.operation.dto.admin.request.SignUpRequestDTO;
import org.sopt.makers.operation.dto.admin.response.LoginResponseDTO;
import org.sopt.makers.operation.dto.admin.response.RefreshResponseDTO;
import org.sopt.makers.operation.dto.admin.response.SignUpResponseDTO;

public interface AdminService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);
    LoginResponseDTO login(LoginRequestDTO userLoginRequestDTO);
    void validateRefreshToken(Long adminId, String requestRefreshToken);
    RefreshResponseDTO refresh(String refreshToken);
}
