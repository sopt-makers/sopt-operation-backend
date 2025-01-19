package org.sopt.makers.operation.web.alarm.service;

import static org.sopt.makers.operation.code.failure.AlarmFailureCode.NOT_FOUND_ALARM;
import static org.sopt.makers.operation.code.failure.AlarmFailureCode.INVALID_ALARM_TARGET_TYPE;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmStatus;
import org.sopt.makers.operation.alarm.domain.AlarmTarget;
import org.sopt.makers.operation.alarm.domain.AlarmTargetType;
import org.sopt.makers.operation.alarm.repository.AlarmRepository;
import org.sopt.makers.operation.client.alarm.dto.InstantAlarmRequest;
import org.sopt.makers.operation.client.alarm.dto.ScheduleAlarmRequest;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.client.alarm.AlarmManager;
import org.sopt.makers.operation.exception.AlarmException;

import org.sopt.makers.operation.web.alarm.dto.request.AlarmInstantSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleStatusUpdateRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmGetResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmCreateResponse;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmListGetResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final AlarmManager alarmManager;
    private final ValueConfig valueConfig;

    @Override
    @Transactional
    public AlarmCreateResponse sendInstantAlarm(AlarmInstantSendRequest request) {
        val alarm = request.toEntity();
        val alarmTarget = alarm.getTarget();

        alarmTarget.setTargetIds(extractTargetIds(alarmTarget));

        val savedAlarm = alarmRepository.save(alarm);
        val alarmRequest = InstantAlarmRequest.of(savedAlarm);

        alarmManager.sendInstant(alarmRequest);
        return AlarmCreateResponse.of(savedAlarm);
    }

    @Override
    @Transactional
    public AlarmCreateResponse sendScheduleAlarm(AlarmScheduleSendRequest request) {
        val alarm = request.toEntity();
        val alarmTarget = alarm.getTarget();
        alarmTarget.setTargetIds(extractTargetIds(alarmTarget));
        val savedAlarm = alarmRepository.save(alarm);

        val scheduleAlarmRequest = ScheduleAlarmRequest.of(savedAlarm);
        alarmManager.sendSchedule(scheduleAlarmRequest);

        return AlarmCreateResponse.of(savedAlarm);
    }

    @Override
    @Transactional(readOnly = true)
    public AlarmGetResponse getAlarm(long alarmId) {
        val alarm = findAlarm(alarmId);
        return AlarmGetResponse.of(alarm);
    }

    @Override
    @Transactional(readOnly = true)
    public AlarmListGetResponse getAlarms(Integer generation, AlarmStatus status, Pageable pageable) {
        val alarms = alarmRepository.findOrderByCreatedDate(generation, status, pageable);
        val totalCount = alarmRepository.count(generation, status);
        return AlarmListGetResponse.of(alarms, totalCount);
    }

    @Override
    @Transactional
    public void deleteAlarm(long alarmId) {
        val alarm = findAlarm(alarmId);
        alarmRepository.delete(alarm);
    }

    @Override
    @Transactional
    public void updateScheduleAlarm(long alarmId, AlarmScheduleStatusUpdateRequest request) {
        val alarm = findAlarm(alarmId);
        alarm.updateStatusToComplete(request.sendAt());
    }

    private Alarm findAlarm(long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new AlarmException(NOT_FOUND_ALARM));
    }

    private List<String> extractTargetIds(AlarmTarget target) {
        val targetType = target.getTargetType();
        if (targetType.equals(AlarmTargetType.CSV)) {
            return target.getTargetIds();
        }

        val members = switch (targetType) {
            case ALL -> memberRepository.findAll();
            case ACTIVE -> memberRepository.find(valueConfig.getGENERATION(), target.getTargetPart().toPartDomain());
            default -> throw new AlarmException(INVALID_ALARM_TARGET_TYPE);
        };
        return members.stream()
                .map(Member::getPlaygroundId)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .toList();
    }
}
