package org.operation.web.admin.dto.response;

public record RefreshResponse(
        String accessToken
) {

    public static RefreshResponse of(String accessToken) {
        return new RefreshResponse(accessToken);
    }
}