package org.sopt.makers.operation.service;

import static java.util.Objects.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.Part.*;
import static org.sopt.makers.operation.entity.alarm.Status.*;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSenderDTO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Status;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.external.api.AlarmSender;
import org.sopt.makers.operation.external.api.PlayGroundServer;
import org.sopt.makers.operation.repository.alarm.AlarmRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.*;

import lombok.*;

import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmResponseDTO;
import org.sopt.makers.operation.dto.alarm.AlarmsResponseDTO;
import org.sopt.makers.operation.entity.alarm.Alarm;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

	private final AlarmRepository alarmRepository;
	private final MemberRepository memberRepository;
	private final AlarmSender alarmSender;
	private final PlayGroundServer playGroundServer;
	private final ValueConfig valueConfig;

	@Override
	@Transactional
	public void sendByAdmin(AlarmSendRequestDTO requestDTO) {
		val alarm = findAlarm(requestDTO.alarmId());
		if (alarm.getStatus().equals(AFTER)) {
			throw new AlarmException(ALREADY_SEND_ALARM.getName());
		}

		val targetIdList = getTargetIdList(alarm);
		alarmSender.send(AlarmSenderDTO.of(alarm, targetIdList));

		alarm.updateStatus();
		alarm.updateSendAt();
	}

	private List<String> getTargetIdList(Alarm alarm) {
		val targetList = alarm.getTargetList();
		if (!targetList.isEmpty()) {
			return targetList;
		}

		val activeTargetList = getActiveTargetList(alarm.getPart());
		if (alarm.getIsActive()) {
			return activeTargetList;
		}

		val inactiveTargetList = getInactiveTargetList(valueConfig.getGENERATION(), alarm.getPart());
		return inactiveTargetList.stream()
			.filter(target -> !activeTargetList.contains(target))
			.toList();
	}

	private List<String> getActiveTargetList(Part part) {
		part = part.equals(ALL) ? null : part;
		val members = memberRepository.search(new MemberSearchCondition(part, valueConfig.getGENERATION()));
		return members.stream()
			.filter(member -> nonNull(member.getPlaygroundId()))
			.map(member -> String.valueOf(member.getPlaygroundId()))
			.toList();
	}

	private List<String> getInactiveTargetList(int generation, Part part) {
		val members = playGroundServer.getInactiveMembers(generation, part);
		return members.memberIds().stream()
			.map(String::valueOf)
			.toList();
	}

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
		val alarmsCount = alarmRepository.countByGenerationAndPartAndStatus(generation, part, status);
		return AlarmsResponseDTO.of(alarms, alarmsCount);
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
