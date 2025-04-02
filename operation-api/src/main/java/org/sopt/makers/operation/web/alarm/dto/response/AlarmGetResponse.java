package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_RESPONSE_DATE_FORMAT;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_RESPONSE_TIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.val;
import org.sopt.makers.operation.alarm.domain.Alarm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Builder(access = PRIVATE)
public record AlarmGetResponse(
        String status,
        String sendType,
        String targetType,
        String targetPart,
        @JsonInclude(value = NON_NULL)
        Integer targetGeneration,
        String createDate,
        String createTime,
        @JsonInclude(value = NON_NULL)
        String intendDate,
        @JsonInclude(value = NON_NULL)
        String intendTime,
        String sendDate,
        String sendTime,
        String title,
        String content,
        String category,
        String link,
        String linkType
) {

    public static AlarmGetResponse of(Alarm alarm) {
        val alarmContent = alarm.getContent();
        val alarmTarget = alarm.getTarget();
        val createdAt = alarm.getCreatedDate();
        val intendedAt = alarm.getIntendedAt();
        val sendAt = alarm.getSendAt();
        return AlarmGetResponse.builder()
                .status(alarm.getStatus().getDescription())
                .sendType(alarm.getType().getDescription())
                .targetType(alarmTarget.getTargetType().getName())
                .targetPart(alarmTarget.getTargetPart().getName())
                .targetGeneration(alarmTarget.getGeneration())
                .createDate(covertToDate(createdAt)).createTime(covertToTime(createdAt))
                .intendDate(covertToDate(intendedAt)).intendTime(covertToTime(intendedAt))
                .sendDate(covertToDate(sendAt)).sendTime(covertToTime(sendAt))
                .category(alarmContent.getCategory().getName())
                .title(alarmContent.getTitle()).content(alarmContent.getContent())
                .link(alarmContent.getLinkPath()).linkType(alarmContent.getLinkType().getName())
                .build();
    }

    private static String covertToDate(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)){
            return null;
        }
        return dateTime.toLocalDate()
                .format(DateTimeFormatter.ofPattern(ALARM_RESPONSE_DATE_FORMAT));
    }

    private static String covertToTime(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)){
            return null;
        }
        return dateTime.toLocalTime()
                .format(DateTimeFormatter.ofPattern(ALARM_RESPONSE_TIME_FORMAT));
    }
}
