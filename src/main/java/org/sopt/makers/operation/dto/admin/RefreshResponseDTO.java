package org.sopt.makers.operation.dto.admin;

public record RefreshResponseDTO(String accessToken) {
    public static RefreshResponseDTO of(String accessToken) {
        return new RefreshResponseDTO(
                accessToken
        );
    }
}
