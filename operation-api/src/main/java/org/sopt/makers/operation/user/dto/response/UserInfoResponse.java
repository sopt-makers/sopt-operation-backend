package org.sopt.makers.operation.user.dto.response;

import java.util.List;

import lombok.Builder;

import org.sopt.makers.operation.user.domain.User;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;

import static lombok.AccessLevel.*;

@Builder(access = PRIVATE)
public record UserInfoResponse(
        Long id,
        String name,
        String phone,
        String profileImage,
        List<ActivityTotalVO> activities
) {
    public static UserInfoResponse of(User user, List<UserGenerationHistory> histories) {
        List<ActivityTotalVO> activities = histories.stream().map(ActivityTotalVO::from).toList();
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .activities(activities)
                .build();
    }
}
