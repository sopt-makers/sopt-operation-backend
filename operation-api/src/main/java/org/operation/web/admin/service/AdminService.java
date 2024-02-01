package org.operation.web.admin.service;

import org.operation.web.admin.dto.request.LoginRequest;
import org.operation.web.admin.dto.request.SignUpRequest;
import org.operation.web.admin.dto.response.LoginResponse;
import org.operation.web.admin.dto.response.RefreshResponse;
import org.operation.web.admin.dto.response.SignUpResponse;

public interface AdminService {
    SignUpResponse signUp(SignUpRequest request);
    LoginResponse login(LoginRequest request);
    RefreshResponse refresh(String refreshToken);
}
