package org.sopt.makers.operation.web.alarm.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.val;

import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmTarget;
import org.sopt.makers.operation.alarm.domain.AlarmContent;
import org.sopt.makers.operation.alarm.domain.AlarmCategory;
import org.sopt.makers.operation.alarm.domain.AlarmTargetType;
import org.sopt.makers.operation.alarm.domain.AlarmTargetPart;
import org.sopt.makers.operation.alarm.domain.AlarmLinkType;

import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_REQUEST_DATE_FORMAT;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_REQUEST_TIME_FORMAT;

public record AlarmScheduleSendRequest(
        @NotNull String title,
        @NotNull String content,
        @NotNull AlarmCategory category,
        @NotNull AlarmTargetType targetType,
        List<String> targetList,
        AlarmTargetPart part,
        Integer createdGeneration,
        AlarmLinkType linkType,
        String link,
        @NotNull String postDate,
        @NotNull String postTime
) {
    private AlarmTarget toTargetEntity() {
        return switch (this.targetType) {
            case ALL -> this.part.equals(AlarmTargetPart.ALL)
                    ? AlarmTarget.all(this.createdGeneration)
                    : AlarmTarget.partialForAll(this.createdGeneration, this.part, this.targetList);
            case ACTIVE -> AlarmTarget.partialForActive(this.createdGeneration, this.part, this.targetList);
            case CSV -> AlarmTarget.partialForCsv(this.createdGeneration, this.targetList);
        };
    }

    private AlarmContent toContentEntity() {
        return switch (this.linkType) {
            case WEB -> AlarmContent.withWebLink(this.title, this.content, this.category, this.link);
            case APP -> AlarmContent.withAppLink(this.title, this.content, this.category, this.link);
            case NONE -> AlarmContent.withoutLink(this.title, this.content, this.category);
        };
    }

    public Alarm toEntity() {
        AlarmTarget targetEntity = this.toTargetEntity();
        AlarmContent contentEntity = this.toContentEntity();
        val date = LocalDate.parse(postDate, DateTimeFormatter.ofPattern(ALARM_REQUEST_DATE_FORMAT));
        val time = LocalTime.parse(postTime, DateTimeFormatter.ofPattern(ALARM_REQUEST_TIME_FORMAT));
        return Alarm.scheduled(targetEntity, contentEntity, LocalDateTime.of(date, time));
    }
}
