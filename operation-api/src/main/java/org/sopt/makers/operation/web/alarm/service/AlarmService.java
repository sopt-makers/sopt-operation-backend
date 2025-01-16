package org.sopt.makers.operation.web.alarm.service;

import org.sopt.makers.operation.alarm.domain.AlarmStatus;

import org.sopt.makers.operation.web.alarm.dto.request.AlarmInstantSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleSendRequest;

import org.sopt.makers.operation.web.alarm.dto.response.AlarmCreateResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmGetResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListGetResponse;

import org.springframework.data.domain.Pageable;

public interface AlarmService {
    
    AlarmCreateResponse sendInstantAlarm(AlarmInstantSendRequest requestDTO);

    AlarmCreateResponse sendScheduleAlarm(AlarmScheduleSendRequest request);

    AlarmListGetResponse getAlarms(Integer generation, AlarmStatus status, Pageable pageable);

    AlarmGetResponse getAlarm(long alarmId);

    void deleteAlarm(long alarmId);
}
