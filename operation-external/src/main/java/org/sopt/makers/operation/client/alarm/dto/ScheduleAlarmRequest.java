package org.sopt.makers.operation.client.alarm.dto;

import lombok.AccessLevel;
import lombok.Builder;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmCategory;
import org.sopt.makers.operation.alarm.domain.AlarmLinkType;
import org.sopt.makers.operation.alarm.domain.AlarmTargetType;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record ScheduleAlarmRequest(
        long alarmId,
        String title,
        String content,
        AlarmCategory category,
        String link,
        AlarmLinkType linkType,
        AlarmTargetType targetType,
        List<String> targets,
        LocalDateTime scheduleDateTime
) implements AlarmRequest {
    public static ScheduleAlarmRequest of(Alarm alarm) {
        return ScheduleAlarmRequest.builder()
                .alarmId(alarm.getId())
                .title(alarm.getContent().getTitle())
                .content(alarm.getContent().getContent())
                .category(alarm.getContent().getCategory())
                .link(alarm.getContent().getLinkPath())
                .linkType(alarm.getContent().getLinkType())
                .targetType(alarm.getTarget().getTargetType())
                .targets(alarm.getTarget().getTargetIds())
                .scheduleDateTime(alarm.getIntendedAt())
                .build();
    }

    public
}
