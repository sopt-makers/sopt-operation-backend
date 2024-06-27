package org.sopt.makers.operation.web.alarm.service;

import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmInstantSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmGetResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListGetResponse;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
    void sendAlarm(AlarmInstantSendRequest requestDTO);

    AlarmListGetResponse getAlarms(Integer generation, Status status, Pageable pageable);

    AlarmGetResponse getAlarm(long alarmId);

    void deleteAlarm(long alarmId);

    void scheduleAlarm(AlarmScheduleSendRequest request);
}
