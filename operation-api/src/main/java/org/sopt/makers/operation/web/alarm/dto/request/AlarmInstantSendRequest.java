package org.sopt.makers.operation.web.alarm.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmTarget;
import org.sopt.makers.operation.alarm.domain.AlarmContent;
import org.sopt.makers.operation.alarm.domain.AlarmCategory;
import org.sopt.makers.operation.alarm.domain.AlarmTargetType;
import org.sopt.makers.operation.alarm.domain.AlarmTargetPart;
import org.sopt.makers.operation.alarm.domain.AlarmLinkType;

public record AlarmInstantSendRequest(
        @NotNull String title,
        @NotNull String content,
        @NotNull AlarmCategory category,
        @NotNull AlarmTargetType targetType,
        AlarmTargetPart part,
        Integer createdGeneration,
        List<String> targetList,
        AlarmLinkType linkType,
        String link
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
        return Alarm.instant(targetEntity, contentEntity);
    }
}
