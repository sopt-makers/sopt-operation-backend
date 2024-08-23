package org.sopt.makers.operation.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserModifyRequest(
        @JsonProperty("name")
        @NotNull(message = ERROR_MESSAGE_FOR_NOT_NULL)
        String userName,
        @JsonProperty("phone")
        @NotNull(message = ERROR_MESSAGE_FOR_NOT_NULL)
        String userPhone,
        @JsonProperty("profileImage")
        @NotNull(message = ERROR_MESSAGE_FOR_NOT_NULL)
        String userProfileImage,
        @JsonProperty("activities")
        @NotEmpty(message = ERROR_MESSAGE_FOR_NOT_EMPTY)
        List<UserActivityModifyRequest> userActivities
) {
        private static final String ERROR_MESSAGE_FOR_NOT_NULL = "는 필수 값입니다.";
        private static final String ERROR_MESSAGE_FOR_NOT_EMPTY = "는 한 개 이상이어야 합니다.";
}
