package org.sopt.makers.operation.client.alarm.dto.eventbridge;

import lombok.Builder;

@Builder
public record AlarmScheduleEventBridgeHeader(
        String action,
        String xApiKey,
        String transactionId,
        String service,
        long alarmId
) {
}
