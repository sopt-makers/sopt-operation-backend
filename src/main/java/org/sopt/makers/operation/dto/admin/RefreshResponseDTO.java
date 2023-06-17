package org.sopt.makers.operation.dto.admin;

public record RefreshResponseDTO(String accessToken, String refreshToken) {
    public static RefreshResponseDTO of(String accessToken, String refreshToken) {
        return new RefreshResponseDTO(
                accessToken,
                refreshToken
        );
    }
}