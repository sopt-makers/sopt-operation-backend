package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import org.sopt.makers.operation.dto.alarm.AlarmInactiveListResponseDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSenderDTO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Status;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.external.api.AlarmSender;
import org.sopt.makers.operation.repository.alarm.AlarmRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

	@Value("${sopt.makers.playground.server}")
	private String playGroundURI;

	@Value("${sopt.makers.playground.token}")
	private String playGroundToken;

	@Value("${sopt.current.generation}")
	private int currentGeneration;

	private final RestTemplate restTemplate = new RestTemplate();
	private final AlarmRepository alarmRepository;
	private final MemberRepository memberRepository;
	private final AlarmSender alarmSender;

	@Override
	@Transactional
	public void sendAdmin(AlarmSendRequestDTO requestDTO) {
		val alarm = alarmRepository.findById(requestDTO.alarmId())
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ALARM.getName()));

		if (alarm.getStatus().equals(Status.AFTER)) {
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
			return alarm.getTargetList();
		}

		if (alarm.getIsActive()) {
			return extractCurrentTargetList(alarm.getPart());
		}

		val currentGenerationIdList = extractCurrentTargetList(alarm.getPart());
		val inactiveGenerationIdList = extractInactiveTargetList(currentGeneration, alarm.getPart())
			.memberIds().stream()
			.map(String::valueOf).toList();

		return inactiveGenerationIdList.stream()
			.filter(item -> !currentGenerationIdList.contains(item))
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

	private List<String> extractCurrentTargetList(Part part) {
		if (part.equals(Part.ALL)) {
			part = null;
		}

		val memberList = memberRepository.search(new MemberSearchCondition(part, currentGeneration));
		return memberList.stream()
			.filter(member -> member.getPlaygroundId() != null)
			.map(member -> String.valueOf(member.getPlaygroundId()))
			.toList();
	}

	private AlarmInactiveListResponseDTO extractInactiveTargetList(int generation, Part part) {
		val getInactiveUserURL =
			playGroundURI + "/internal/api/v1/members/inactivity?generation=" + generation;

		if (!part.equals(Part.ALL)) {
			getInactiveUserURL.concat("&part=" + part);
		}

		val headers = new HttpHeaders();
		headers.add("content-type", "application/json;charset=UTF-8");
		headers.add("Authorization", playGroundToken);

		val entity = new HttpEntity<>(null, headers);

		try {
			val response = restTemplate.exchange(
				getInactiveUserURL,
				HttpMethod.GET,
				entity,
				AlarmInactiveListResponseDTO.class
			);

			return response.getBody();
		} catch (Exception e) {
			throw new AlarmException(FAIL_INACTIVE_USERS.getName());
		}
	}

	private Alarm findAlarm(Long alarmId) {
		return alarmRepository.findById(alarmId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ALARM.getName()));
	}
}
