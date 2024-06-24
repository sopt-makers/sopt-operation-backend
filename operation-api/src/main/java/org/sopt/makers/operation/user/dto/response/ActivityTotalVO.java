package org.sopt.makers.operation.user.dto.response;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.user.domain.Part;
import org.sopt.makers.operation.user.domain.Team;
import org.sopt.makers.operation.user.domain.Position;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;

import static lombok.AccessLevel.*;

@Builder(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
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
