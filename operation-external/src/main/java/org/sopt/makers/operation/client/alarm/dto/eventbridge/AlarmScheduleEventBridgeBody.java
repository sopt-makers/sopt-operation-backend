package org.sopt.makers.operation.client.alarm.dto.eventbridge;

import java.util.List;

import lombok.Builder;
import org.sopt.makers.operation.alarm.domain.AlarmCategory;

@Builder
public record AlarmScheduleEventBridgeBody(
        List<String> userIds,
        String title,
        String content,
        AlarmCategory category,
        String deepLink,
        String webLink
) {
}
