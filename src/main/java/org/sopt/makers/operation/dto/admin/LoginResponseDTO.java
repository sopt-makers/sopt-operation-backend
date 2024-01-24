package org.sopt.makers.operation.dto.admin;

import lombok.Builder;
import org.sopt.makers.operation.entity.admin.Admin;
import org.sopt.makers.operation.entity.admin.AdminStatus;

@Builder
public record LoginResponseDTO(LoginResponseVO loginResponseVO, String refreshToken) {
    public static LoginResponseDTO of(Admin admin, String accessToken, String refreshToken) {
        return LoginResponseDTO.builder()
                .loginResponseVO(LoginResponseVO.of(admin.getId(), admin.getName(), admin.getStatus(), accessToken))
                .refreshToken(refreshToken)
                .build();
    }
}

@Builder
record LoginResponseVO(Long id, String name, AdminStatus adminStatus, String accessToken) {
    public static LoginResponseVO of(Long id, String name, AdminStatus adminStatus, String accessToken) {
        return LoginResponseVO.builder()
                .id(id)
                .name(name)
                .adminStatus(adminStatus)
                .accessToken(accessToken)
                .build();
    }
}
