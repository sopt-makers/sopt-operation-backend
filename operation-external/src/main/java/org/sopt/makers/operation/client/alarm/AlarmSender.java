package org.sopt.makers.operation.client.alarm;

import org.sopt.makers.operation.client.alarm.dto.AlarmRequest;

interface AlarmSender {
    void sendAlarm(AlarmRequest alarmRequest);
}
