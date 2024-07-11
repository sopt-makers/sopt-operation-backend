package org.sopt.makers.operation.user.dto.request;

import jakarta.validation.constraints.NotNull;
import org.sopt.makers.operation.user.domain.Team;
import org.sopt.makers.operation.user.domain.Part;
import org.sopt.makers.operation.user.domain.Position;

public record UserActivityModifyRequest(

        @NotNull(message = ERROR_MESSAGE_FOR_NOT_NULL)
        long activityId,
        @NotNull(message = ERROR_MESSAGE_FOR_NOT_NULL)
        int generation,
        @NotNull(message = ERROR_MESSAGE_FOR_NOT_NULL)
        Part part,
        Team team,
        Position position
) {
    private static final String ERROR_MESSAGE_FOR_NOT_NULL = "는 필수 값입니다.";
}
