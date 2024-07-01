package org.sopt.makers.operation.client.alarm.eventBridgeServer;


import org.sopt.makers.operation.client.alarm.eventBridgeServer.dto.EventBridgeSenderRequest;

public interface EventBridgeSender {
    void scheduleAlarm(EventBridgeSenderRequest request, String postDate, String postTime, Long alarmId);

}
