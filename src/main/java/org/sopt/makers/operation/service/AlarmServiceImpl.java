package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.entity.alarm.Alarm;
import org.sopt.makers.operation.repository.AlarmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
	private final AlarmRepository alarmRepository;

	@Override
	@Transactional
	public Long createAlarm(AlarmRequestDTO requestDTO) {
		val alarmEntity = requestDTO.toEntity();
		val savedAlarm = alarmRepository.save(alarmEntity);
		return savedAlarm.getId();
	}
}
