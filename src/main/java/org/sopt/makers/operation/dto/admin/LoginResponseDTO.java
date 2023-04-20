package org.sopt.makers.operation.dto.admin;

import org.sopt.makers.operation.entity.Admin;

public record LoginResponseDTO(LoginResponseVO loginResponseVO, String refreshToken) {
    public static LoginResponseDTO of(Admin admin, String accessToken, String refreshToken) {
        return new LoginResponseDTO(
                LoginResponseVO.of(admin.getId(), admin.getName(), accessToken),
                refreshToken
        );
    }
}

record LoginResponseVO(Long id, String name, String accessToken) {
    public static LoginResponseVO of(Long id, String name, String accessToken) {
        return new LoginResponseVO(
                id, name, accessToken
        );
    }
}
