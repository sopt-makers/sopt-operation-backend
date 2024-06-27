package org.sopt.makers.operation.user.dto.response;

import java.util.List;

import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record UserInfosResponse(
        List<UserInfoResponse> users
) {
    public static UserInfosResponse of(List<UserInfoResponse> userInfos) {
        return UserInfosResponse.builder()
                .users(userInfos)
                .build();
    }
}
