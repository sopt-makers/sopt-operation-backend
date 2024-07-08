package org.sopt.makers.operation.client.alarm.alarmServer;

import org.sopt.makers.operation.client.alarm.alarmServer.dto.AlarmSenderRequest;

public interface AlarmSender {
    void send(AlarmSenderRequest request);
}
