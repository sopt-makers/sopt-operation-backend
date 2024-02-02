package org.operation.web.alarm.service;

import static java.util.Objects.*;
import static org.operation.alarm.domain.Status.*;
import static org.operation.alarm.message.ErrorMessage.*;

import java.util.List;

import org.operation.alarm.domain.Alarm;
import org.operation.alarm.domain.Status;
import org.operation.alarm.repository.AlarmRepository;
import org.operation.client.alarm.AlarmSender;
import org.operation.client.playground.PlayGroundServer;
import org.operation.common.config.ValueConfig;
import org.operation.common.domain.Part;
import org.operation.common.dto.MemberSearchCondition;
import org.operation.common.exception.AlarmException;
import org.operation.member.repository.MemberRepository;
import org.operation.web.alarm.dto.request.AlarmRequest;
import org.operation.web.alarm.dto.request.AlarmSendRequest;
import org.operation.web.alarm.dto.request.AlarmSenderRequest;
import org.operation.web.alarm.dto.response.AlarmResponse;
import org.operation.web.alarm.dto.response.AlarmsResponse;
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
	public void sendByAdmin(AlarmSendRequest request) {
		val alarm = findAlarm(request.alarmId());

		if (alarm.getStatus().equals(AFTER)) {
			throw new AlarmException(FAIL_SEND_ALARM.getContent());
		}

		val targetIdList = getTargetIdList(alarm);
		alarmSender.send(AlarmSenderRequest.of(alarm, targetIdList));

		alarm.updateStatus();
		alarm.updateSendAt();
	}

	private Alarm findAlarm(long id) {
		return alarmRepository.findById(id)
				.orElseThrow(() -> new AlarmException(INVALID_ALARM.getContent()));
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
		val generation = valueConfig.getGENERATION();
		val members = memberRepository.search(new MemberSearchCondition(getPart(part), generation));
		return members.stream()
			.filter(member -> nonNull(member.getPlaygroundId()))
			.map(member -> String.valueOf(member.getPlaygroundId()))
			.toList();
	}

	private Part getPart(Part part) {
		return part.equals(Part.ALL) ? null : part;
	}

	private List<String> getInactiveTargetList(int generation, Part part) {
		val members = playGroundServer.getInactiveMembers(generation, part);
		return members.memberIds().stream()
			.map(String::valueOf)
			.toList();
	}

	@Override
	@Transactional
	public Long createAlarm(AlarmRequest request) {
		val alarmEntity = request.toEntity();
		val savedAlarm = alarmRepository.save(alarmEntity);
		return savedAlarm.getId();
	}

	@Override
	public AlarmsResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable) {
		val alarms = alarmRepository.getAlarms(generation, part, status, pageable);
		val alarmsCount = alarmRepository.countByGenerationAndPartAndStatus(generation, part, status);
		return AlarmsResponse.of(alarms, alarmsCount);
	}

	@Override
	public AlarmResponse getAlarm(long alarmId) {
		val alarm = findAlarm(alarmId);
		return AlarmResponse.of(alarm);
	}

	@Override
	@Transactional
	public void deleteAlarm(Long alarmId) {
		val alarm = findAlarm(alarmId);
		alarmRepository.delete(alarm);
	}
}
