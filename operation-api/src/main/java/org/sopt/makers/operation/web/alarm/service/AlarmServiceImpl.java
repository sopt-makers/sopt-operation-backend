package org.sopt.makers.operation.web.alarm.service;

import static java.util.Objects.*;
import static org.sopt.makers.operation.code.failure.AlarmFailureCode.*;

import java.util.List;

import org.sopt.makers.operation.client.alarm.AlarmSender;
import org.sopt.makers.operation.client.alarm.dto.AlarmSenderRequest;
import org.sopt.makers.operation.client.playground.PlayGroundServer;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.alarm.domain.Alarm;
import org.sopt.makers.operation.domain.alarm.domain.Status;
import org.sopt.makers.operation.domain.alarm.repository.AlarmRepository;
import org.sopt.makers.operation.domain.member.repository.MemberRepository;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmRequest;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListResponse;
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
		val alarm = getAlarmReadyToSend(request.alarmId());
		val targetIdList = getTargets(alarm);
		sendAlarmToTargets(alarm, targetIdList);
	}

	private Alarm getAlarmReadyToSend(long alarmId) {
		val alarm = findAlarm(alarmId);
		checkAlarmReadyToSend(alarm);
		return alarm;
	}

	private Alarm findAlarm(long id) {
		return alarmRepository.findById(id)
				.orElseThrow(() -> new AlarmException(INVALID_ALARM));
	}

	private void checkAlarmReadyToSend(Alarm alarm) {
		if (alarm.isSent()) {
			throw new AlarmException(FAIL_SEND_ALARM);
		}
	}

	private List<String> getTargets(Alarm alarm) {
		if (alarm.hasEmptyTargetList()) {
			return alarm.getTargetList();
		}
		return getTargetsByActivityStatus(alarm.getIsActive(), alarm.getPart());
	}

	private List<String> getTargetsByActivityStatus(boolean isActive, Part part) {
		if (isActive) {
			return getActiveTargets(part);
		} else {
			return getInactiveTargets(part);
		}
	}

	private List<String> getActiveTargets(Part part) {
		val generation = valueConfig.getGENERATION();
		val memberList = memberRepository.find(generation, part);
		return memberList.stream()
			.filter(member -> nonNull(member.getPlaygroundId()))
			.map(member -> String.valueOf(member.getPlaygroundId()))
			.toList();
	}

	private List<String> getInactiveTargets(Part part) {
		val generation = valueConfig.getGENERATION();
		val memberList = getInactiveMemberIdsFromPlayground(generation, part);
		val activeTargetList = getActiveTargets(part);
		return memberList.stream()
				.filter(target -> !activeTargetList.contains(target))
				.toList();
	}

	private List<String> getInactiveMemberIdsFromPlayground(int generation, Part part) {
		val memberIdList = playGroundServer.getInactiveMembers(generation, part).memberIds();
		return memberIdList.stream()
				.map(String::valueOf)
				.toList();
	}

	private void sendAlarmToTargets(Alarm alarm, List<String> targetIdList) {
		val alarmRequest = AlarmSenderRequest.of(alarm, targetIdList);
		alarmSender.send(alarmRequest);
		alarm.updateToSent();
	}

	@Override
	@Transactional
	public long createAlarm(AlarmRequest request) {
		val alarmEntity = request.toEntity();
		val savedAlarm = alarmRepository.save(alarmEntity);
		return savedAlarm.getId();
	}

	@Override
	public AlarmListResponse getAlarms(Integer generation, Part part, Status status, Pageable pageable) {
		val alarmList = alarmRepository.findOrderByCreatedDate(generation, part, status, pageable);
		val totalCount = alarmRepository.count(generation, part, status);
		return AlarmListResponse.of(alarmList, totalCount);
	}

	@Override
	public AlarmResponse getAlarm(long alarmId) {
		val alarm = findAlarm(alarmId);
		return AlarmResponse.of(alarm);
	}

	@Override
	@Transactional
	public void deleteAlarm(long alarmId) {
		val alarm = findAlarm(alarmId);
		alarmRepository.delete(alarm);
	}
}
