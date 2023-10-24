package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;

public interface AlarmService {
	Long createAlarm(AlarmRequestDTO requestDTO);
}
