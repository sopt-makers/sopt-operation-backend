package org.sopt.makers.operation.client.alarm.dto;

import lombok.Builder;
import lombok.val;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmCategory;
import org.sopt.makers.operation.alarm.domain.AlarmLinkType;
import org.sopt.makers.operation.alarm.domain.AlarmTargetType;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.sopt.makers.operation.alarm.domain.AlarmCategory.NEWS;

@Builder(access = PRIVATE)
public record InstantAlarmRequest(
    String title,
    String content,
    AlarmTargetType targetType,
    List<String> targets,
    AlarmCategory category,
    String link,
    AlarmLinkType linkType

) implements AlarmRequest {

    public static InstantAlarmRequest of(Alarm alarm) {
        val content = alarm.getContent();
        return InstantAlarmRequest.builder()
                .targetType(alarm.getTarget().getTargetType())
                .targets(alarm.getTarget().getTargetIds())
                .title(content.getTitle())
                .content(content.getContent())
                .category(content.getCategory())
                .linkType(content.getLinkType()).link(content.getLinkPath())
                .build();
    }

    public static InstantAlarmRequest of(String title, String content, List<String> targetList) {
        return InstantAlarmRequest.builder()
                .targets(targetList)
                .title(title)
                .content(content)
                .category(NEWS)
                .build();
    }

}
