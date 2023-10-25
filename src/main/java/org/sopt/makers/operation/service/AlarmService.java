package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmResponseDTO;
import org.sopt.makers.operation.dto.alarm.AlarmsResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Status;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
	Long createAlarm(AlarmRequestDTO requestDTO);
	AlarmsResponseDTO getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	AlarmResponseDTO getAlarm(Long alarmId);
	void deleteAlarm(Long alarmId);
}
