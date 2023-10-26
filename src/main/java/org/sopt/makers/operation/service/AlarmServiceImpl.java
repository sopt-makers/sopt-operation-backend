package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmResponseDTO;
import org.sopt.makers.operation.dto.alarm.AlarmsResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Alarm;
import org.sopt.makers.operation.entity.alarm.Status;
import org.sopt.makers.operation.repository.alarm.AlarmRepository;
import org.springframework.data.domain.Pageable;
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

	@Override
	public AlarmsResponseDTO getAlarms(Integer generation, Part part, Status status, Pageable pageable) {
		val alarms = alarmRepository.getAlarms(generation, part, status, pageable);
		return AlarmsResponseDTO.of(alarms);
	}

	@Override
	public AlarmResponseDTO getAlarm(Long alarmId) {
		val alarm = findAlarm(alarmId);
		return AlarmResponseDTO.of(alarm);
	}

	@Override
	@Transactional
	public void deleteAlarm(Long alarmId) {
		val alarm = findAlarm(alarmId);
		alarmRepository.delete(alarm);
	}

	private Alarm findAlarm(Long alarmId) {
		return alarmRepository.findById(alarmId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ALARM.getName()));
	}
}
