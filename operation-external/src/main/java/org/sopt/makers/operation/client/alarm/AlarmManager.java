package org.sopt.makers.operation.client.alarm;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.client.alarm.alarmServer.AlarmSenderImpl;
import org.sopt.makers.operation.client.alarm.alarmServer.dto.AlarmSenderRequest;
import org.sopt.makers.operation.client.alarm.eventBridgeServer.EventBridgeSenderImpl;
import org.sopt.makers.operation.client.alarm.eventBridgeServer.dto.EventBridgeSenderRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmManager {

    private final AlarmSenderImpl alarmSender;
    private final EventBridgeSenderImpl eventBridgeSender;

    public void postInstantAlarm(AlarmSenderRequest request) {
        alarmSender.send(request);
    }

    public void postReservedAlarm(EventBridgeSenderRequest request, String postDate, String postTime, Long alarmId) {
        eventBridgeSender.scheduleAlarm(request, postDate, postTime, alarmId);
    }
}
