package org.sopt.makers.operation.web.alarm.service;

import static org.sopt.makers.operation.code.failure.AlarmFailureCode.NOT_FOUND_ALARM;
import static org.sopt.makers.operation.code.failure.AlarmFailureCode.INVALID_ALARM_TARGET_TYPE;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_RESPONSE_DATE_FORMAT;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_RESPONSE_TIME_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final String DATETIME_FORMAT = String.join(" ", ALARM_RESPONSE_DATE_FORMAT, ALARM_RESPONSE_TIME_FORMAT);

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

        val alarmRequest = InstantAlarmRequest.of(alarm);

        alarmManager.sendInstant(alarmRequest);
        alarm.updateStatusToComplete(LocalDateTime.now());
        val savedAlarm = alarmRepository.save(alarm);
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

    // 외부 API 호출이 트랜잭션 지연 및 성능 저하를 유발할 수 있으나,
    // 현재는 도메인 정책상 정합성을 우선시하기 위해 외부 API 호출을 트랜잭션 내에서 함께 처리합니다.
    // 향후 보상 트랜잭션 또는 비동기 처리를 고려할 수 있습니다.
    // @sung-silver
    @Override
    @Transactional
    public void deleteAlarm(long alarmId) {
        val alarm = findAlarm(alarmId);
        val isScheduledAlarm = alarm.getStatus().equals(AlarmStatus.SCHEDULED);

        alarmRepository.delete(alarm);
        if (isScheduledAlarm) {
            alarmManager.deleteSchedule(alarmId, alarm.getIntendedAt());
        }
    }

    @Override
    @Transactional
    public void updateScheduleAlarm(long alarmId, AlarmScheduleStatusUpdateRequest request) {
        val alarm = findAlarm(alarmId);
        val sendAtDateTime = LocalDateTime.parse(request.sendAt(), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
        alarm.updateStatusToComplete(sendAtDateTime);
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
