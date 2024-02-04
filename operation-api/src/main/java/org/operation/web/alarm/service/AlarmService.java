package org.operation.web.alarm.service;

import org.operation.alarm.domain.Status;
import org.operation.common.domain.Part;
import org.operation.web.alarm.dto.request.AlarmRequest;
import org.operation.web.alarm.dto.request.AlarmSendRequest;
import org.operation.web.alarm.dto.response.AlarmResponse;
import org.operation.web.alarm.dto.response.AlarmsResponse;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
	void sendByAdmin(AlarmSendRequest request);
	Long createAlarm(AlarmRequest requestDTO);
	AlarmsResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	AlarmResponse getAlarm(long alarmId);
	void deleteAlarm(Long alarmId);
}
