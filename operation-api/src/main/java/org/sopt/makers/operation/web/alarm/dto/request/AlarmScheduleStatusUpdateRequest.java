package org.sopt.makers.operation.web.alarm.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AlarmScheduleStatusUpdateRequest(
        @NotNull LocalDateTime sendAt
) {
}
