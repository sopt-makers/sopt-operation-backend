package org.sopt.makers.operation.web.alarm.service;

import static org.sopt.makers.operation.code.failure.AlarmFailureCode.*;

import java.util.List;
import java.util.Objects;

import org.sopt.makers.operation.client.alarm.AlarmSender;
import org.sopt.makers.operation.client.alarm.dto.AlarmSenderRequest;
import org.sopt.makers.operation.client.playground.PlayGroundServer;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.alarm.repository.AlarmRepository;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmCreateRequest;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmCreateResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmGetResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListGetResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

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
	public void sendAlarm(AlarmSendRequest request) {
		val alarm = getAlarmReadyToSend(request.alarmId());
		val targets = getTargets(alarm);
		alarmSender.send(AlarmSenderRequest.of(alarm, targets));
		alarm.updateToSent();
	}

	@Override
	@Transactional
	public AlarmCreateResponse saveAlarm(AlarmCreateRequest request) {
		val savedAlarm = alarmRepository.save(request.toEntity());
		return AlarmCreateResponse.of(savedAlarm);
	}

	@Override
	public AlarmListGetResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable) {
		val alarms = alarmRepository.findOrderByCreatedDate(generation, part, status, pageable);
		val totalCount = alarmRepository.count(generation, part, status);
		return AlarmListGetResponse.of(alarms, totalCount);
	}

	@Override
	public AlarmGetResponse getAlarm(long alarmId) {
		val alarm = findAlarm(alarmId);
		return AlarmGetResponse.of(alarm);
	}

	@Override
	@Transactional
	public void deleteAlarm(long alarmId) {
		val alarm = findAlarm(alarmId);
		alarmRepository.delete(alarm);
	}

	private Alarm getAlarmReadyToSend(long alarmId) {
		val alarm = findAlarm(alarmId);
		if (alarm.isSent()) {
			throw new AlarmException(SENT_ALARM);
		}
		return alarm;
	}

	private Alarm findAlarm(long id) {
		return alarmRepository.findById(id)
				.orElseThrow(() -> new AlarmException(INVALID_ALARM));
	}

	private List<String> getTargets(Alarm alarm) {
		return alarm.hasTargets()
				? alarm.getTargetList()
				: getTargetsByActivityAndPart(alarm.getIsActive(), alarm.getPart());
	}

	private List<String> getTargetsByActivityAndPart(boolean isActive, Part part) {
		return isActive ? getActiveTargets(part) : getInactiveTargets(part);
	}

	private List<String> getActiveTargets(Part part) {
		val generation = valueConfig.getGENERATION();
		val members = memberRepository.find(generation, part);
		return members.stream()
				.map(Member::getPlaygroundId)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.toList();
	}

	private List<String> getInactiveTargets(Part part) {
		val generation = valueConfig.getGENERATION();
		val activePlaygroundIds = getActiveTargets(part);
		return getPlaygroundIds(generation, part).stream()
				.filter(id -> !activePlaygroundIds.contains(id))
				.toList();
	}

	private List<String> getPlaygroundIds(int generation, Part part) {
		val members = playGroundServer.getMembers(generation, part);
		return members.memberIds().stream()
				.map(String::valueOf)
				.toList();
	}
}
