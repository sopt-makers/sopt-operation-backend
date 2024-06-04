package org.sopt.makers.operation.auth.dto.request;

public record AccessTokenRequest(
        String grantType,
        String clientId,
        String redirectUri,
        String code,
        String refreshToken
) {
    public boolean isNullGrantType() {
        return grantType == null;
    }

    public boolean isNullCode() {
        return code == null;
    }

    public boolean isNullRefreshToken() {
        return refreshToken == null;
    }
}
