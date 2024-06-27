package org.sopt.makers.operation.user.dto.response;

import lombok.Builder;

import org.sopt.makers.operation.user.domain.Part;
import org.sopt.makers.operation.user.domain.Team;
import org.sopt.makers.operation.user.domain.Position;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record UserActivityInfoResponse(
        long id,
        int generation,
        Part part,
        Team team,
        Position position
) {
    public static UserActivityInfoResponse from(UserGenerationHistory history) {
        return UserActivityInfoResponse.builder()
                .id(history.getId())
                .generation(history.getGeneration())
                .part(history.getPart())
                .team(history.getTeam())
                .position(history.getPosition())
                .build();
    }
}
