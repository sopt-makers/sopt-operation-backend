package org.sopt.makers.operation.user.dto.response;

import lombok.Builder;

import org.sopt.makers.operation.user.domain.Part;
import org.sopt.makers.operation.user.domain.Team;
import org.sopt.makers.operation.user.domain.Position;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;

import static lombok.AccessLevel.*;

@Builder(access = PRIVATE)
public record ActivityTotalVO(
        int generation,
        Part part,
        Team team,
        Position position
) {
    public static ActivityTotalVO from(UserGenerationHistory history) {
        return ActivityTotalVO.builder()
                .generation(history.getGeneration())
                .part(history.getPart())
                .team(history.getTeam())
                .position(history.getPosition())
                .build();
    }
}
