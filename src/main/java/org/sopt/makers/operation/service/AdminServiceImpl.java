package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.admin.LoginRequestDTO;
import org.sopt.makers.operation.dto.admin.LoginResponseDTO;
import org.sopt.makers.operation.dto.admin.SignUpRequestDTO;
import org.sopt.makers.operation.dto.admin.SignUpResponseDTO;

public interface AdminServiceImpl {
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);
    public LoginResponseDTO login(LoginRequestDTO userLoginRequestDTO);
}
