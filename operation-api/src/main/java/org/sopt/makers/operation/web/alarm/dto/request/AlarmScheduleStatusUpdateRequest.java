package org.sopt.makers.operation.web.alarm.dto.request;

import jakarta.validation.constraints.NotNull;

public record AlarmScheduleStatusUpdateRequest(
        @NotNull String sendAt
) {
}
