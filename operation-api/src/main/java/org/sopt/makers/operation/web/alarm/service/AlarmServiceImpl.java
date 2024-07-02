package org.sopt.makers.operation.web.alarm.service;

import static org.sopt.makers.operation.code.failure.AlarmFailureCode.INVALID_ALARM;
import static org.sopt.makers.operation.code.failure.AlarmFailureCode.INVALID_ALARM_TARGET_TYPE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.LinkType;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.alarm.domain.TargetType;
import org.sopt.makers.operation.alarm.repository.AlarmRepository;
import org.sopt.makers.operation.client.alarm.AlarmManager;
import org.sopt.makers.operation.client.alarm.alarmServer.dto.AlarmSenderRequest;
import org.sopt.makers.operation.client.alarm.eventBridgeServer.dto.EventBridgeSenderRequest;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmInstantSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleSendRequest;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmGetResponse;
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
    public void sendInstantAlarm(AlarmInstantSendRequest request) {
        val savedAlarm = alarmRepository.save(request.toEntity());
        val targets = getTargets(savedAlarm);
        alarmManager.sendInstantAlarm(AlarmSenderRequest.of(savedAlarm, targets));
        savedAlarm.updateToSent(formatSendAt(LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void sendScheduleAlarm(AlarmScheduleSendRequest request) {
        val savedAlarm = alarmRepository.save(request.toEntity());
        val sendAt = parseDateTime(request.postDate(), request.postTime());
        savedAlarm.updateToSent(formatSendAt(sendAt));

        val linkType = request.linkType();
        val webLink = linkType == LinkType.WEB ? request.link() : null;
        val deepLink = linkType == LinkType.APP ? request.link() : null;

        val eventBridgeRequest = EventBridgeSenderRequest.of(
                valueConfig.getNOTIFICATION_KEY(),
                valueConfig.getNOTIFICATION_HEADER_SERVICE(),
                request.targetType().getAction(),
                request.targetList(),
                request.title(),
                request.content(),
                request.category(),
                deepLink,
                webLink
        );

        alarmManager.sendReservedAlarm(eventBridgeRequest, request.postDate(), request.postTime(), savedAlarm.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public AlarmListGetResponse getAlarms(Integer generation, Status status, Pageable pageable) {
        val alarms = alarmRepository.findOrderByCreatedDate(generation, status, pageable);
        val totalCount = alarmRepository.count(generation, status);
        return AlarmListGetResponse.of(alarms, totalCount);
    }

    @Override
    @Transactional(readOnly = true)
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

    private Alarm findAlarm(long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new AlarmException(INVALID_ALARM));
    }

    private List<String> getTargets(Alarm alarm) {
        return alarm.hasTargets()
                ? alarm.getTargetList()
                : getTargetsByActivityAndPart(alarm.getTargetType(), alarm.getPart());
    }

    private List<String> getTargetsByActivityAndPart(TargetType targetType, Part part) {
        val members = switch (targetType) {
            case ACTIVE -> memberRepository.find(valueConfig.getGENERATION(), part);
            case ALL -> memberRepository.findAll();
            default -> throw new AlarmException(INVALID_ALARM_TARGET_TYPE);
        };

        return members.stream()
                .map(Member::getPlaygroundId)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .toList();
    }

    private LocalDateTime parseDateTime(String postDate, String postTime) {
        val date = LocalDate.parse(postDate, DateTimeFormatter.ISO_LOCAL_DATE);
        val time = LocalTime.parse(postTime, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, time);
    }

    private String formatSendAt(LocalDateTime dateTime) {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return dateTime.format(formatter);
    }
}
