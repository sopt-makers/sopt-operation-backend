package org.sopt.makers.operation.web.admin.service;

import org.sopt.makers.operation.web.admin.dto.request.LoginRequest;
import org.sopt.makers.operation.web.admin.dto.request.PasswordChangeRequest;
import org.sopt.makers.operation.web.admin.dto.request.SignUpRequest;
import org.sopt.makers.operation.web.admin.dto.response.LoginResponse;
import org.sopt.makers.operation.web.admin.dto.response.TokenRefreshGetResponse;
import org.sopt.makers.operation.web.admin.dto.response.SignUpResponse;

public interface AdminService {
    SignUpResponse signUp(SignUpRequest request);
    LoginResponse login(LoginRequest request);
    TokenRefreshGetResponse refresh(String refreshToken);
    void changePassword(Long adminId, PasswordChangeRequest request);

}
