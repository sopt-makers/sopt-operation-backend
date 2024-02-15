package org.sopt.makers.operation.web.alarm.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmCreateRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmCreateResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmGetResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListGetResponse;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
	void sendAlarm(AlarmSendRequest request);
	AlarmCreateResponse saveAlarm(AlarmCreateRequest requestDTO);
	AlarmListGetResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	AlarmGetResponse getAlarm(long alarmId);
	void deleteAlarm(long alarmId);
}
