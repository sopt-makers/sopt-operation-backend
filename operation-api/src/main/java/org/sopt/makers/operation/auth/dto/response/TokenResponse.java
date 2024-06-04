package org.sopt.makers.operation.auth.dto.response;

public record TokenResponse(String tokenType, String accessToken, String refreshToken) {
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse(BEARER_TOKEN_TYPE, accessToken, refreshToken);
    }
}
