package org.sopt.makers.operation.web.alarm.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.val;
import org.sopt.makers.operation.alarm.domain.*;
import org.sopt.makers.operation.code.failure.AlarmFailureCode;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.exception.AlarmException;

import static org.sopt.makers.operation.code.failure.AlarmFailureCode.INVALID_SEND_SCHEDULED_REQUEST;


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
                    ? AlarmTarget.all()
                    : AlarmTarget.partialForAll(this.part, this.targetList);
            case ACTIVE -> AlarmTarget.partialForActive(this.createdGeneration, this.part, this.targetList);
            case CSV -> AlarmTarget.partialForCsv(this.targetList);
            default -> throw new AlarmException(INVALID_SEND_SCHEDULED_REQUEST);
        };
    }

    private AlarmContent toContentEntity() {
        return switch (this.linkType) {
            case WEB -> AlarmContent.withWebLink(this.title, this.content, this.category, this.link);
            case APP -> AlarmContent.withAppLink(this.title, this.content, this.category, this.link);
            case NONE -> AlarmContent.withoutLink(this.title, this.content, this.category);
            default -> throw new AlarmException(INVALID_SEND_SCHEDULED_REQUEST);
        };
    }

    public Alarm toEntity() {
        AlarmTarget targetEntity = this.toTargetEntity();
        AlarmContent contentEntity = this.toContentEntity();
        val date = LocalDate.parse(postDate, DateTimeFormatter.ISO_LOCAL_DATE);
        val time = LocalTime.parse(postTime, DateTimeFormatter.ofPattern("HH:mm"));
        return Alarm.scheduled(targetEntity, contentEntity, LocalDateTime.of(date, time));
    }
}

