package org.sopt.makers.operation.client.alarm;

import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.client.alarm.dto.InstantAlarmRequest;
import org.sopt.makers.operation.client.alarm.dto.ScheduleAlarmRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AlarmManager {
    private final InstantAlarmSender instantAlarmSender;
    private final ScheduleAlarmSender scheduleAlarmSender;
    private final ScheduleAlarmDeleter scheduleAlarmDeleter;

    public void sendInstant(InstantAlarmRequest alarmRequest) {
        instantAlarmSender.sendAlarm(alarmRequest);
    }

    public void sendSchedule(ScheduleAlarmRequest alarmRequest) {
        scheduleAlarmSender.sendAlarm(alarmRequest);
    }

    public void deleteSchedule(long alarmId, LocalDateTime scheduleDateTime) {
        scheduleAlarmDeleter.deleteAlarm(alarmId, scheduleDateTime);
    }

}
