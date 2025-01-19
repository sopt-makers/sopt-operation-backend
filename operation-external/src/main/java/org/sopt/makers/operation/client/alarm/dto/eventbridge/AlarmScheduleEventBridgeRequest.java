package org.sopt.makers.operation.client.alarm.dto.eventbridge;

public record AlarmScheduleEventBridgeRequest(
        AlarmScheduleEventBridgeHeader header,
        AlarmScheduleEventBridgeBody body
) {
    public static AlarmScheduleEventBridgeRequest of(
            AlarmScheduleEventBridgeHeader header,
            AlarmScheduleEventBridgeBody body
    ) {
        return new AlarmScheduleEventBridgeRequest(header, body);
    }
}
