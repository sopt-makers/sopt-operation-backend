package org.sopt.makers.operation.service.web.admin.dto.response;

import org.operation.admin.domain.Admin;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {

    public static RefreshResponse of(String accessToken, Admin admin) {
        return new RefreshResponse(accessToken, admin.getRefreshToken());
    }
}