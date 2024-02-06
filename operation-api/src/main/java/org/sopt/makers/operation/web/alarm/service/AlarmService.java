package org.sopt.makers.operation.web.alarm.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListResponse;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
	void sendByAdmin(AlarmSendRequest request);
	long createAlarm(AlarmRequest requestDTO);
	AlarmListResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	AlarmResponse getAlarm(long alarmId);
	void deleteAlarm(long alarmId);
}
