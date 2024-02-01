package org.sopt.makers.operation.service.web.alarm;

import org.sopt.makers.operation.dto.alarm.request.AlarmSendRequestDTO;

import org.sopt.makers.operation.dto.alarm.request.AlarmRequestDTO;
import org.sopt.makers.operation.dto.alarm.response.AlarmResponseDTO;
import org.sopt.makers.operation.dto.alarm.response.AlarmsResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.operation.alarm.Status;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
	void sendByAdmin(AlarmSendRequestDTO requestDTO);
	Long createAlarm(AlarmRequestDTO requestDTO);
	AlarmsResponseDTO getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	AlarmResponseDTO getAlarm(Long alarmId);
	void deleteAlarm(Long alarmId);
}
