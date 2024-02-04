package org.sopt.makers.operation.web.alarm.service;

import org.operation.alarm.domain.Status;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.dto.AlarmRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmsResponse;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
	void sendByAdmin(AlarmSendRequest request);
	Long createAlarm(AlarmRequest requestDTO);
	AlarmsResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	AlarmResponse getAlarm(long alarmId);
	void deleteAlarm(Long alarmId);
}