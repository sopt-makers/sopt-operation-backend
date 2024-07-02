package org.sopt.makers.operation.web.alarm.dto.request;

import java.util.List;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmType;
import org.sopt.makers.operation.alarm.domain.Category;
import org.sopt.makers.operation.alarm.domain.LinkType;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.alarm.domain.TargetType;
import org.sopt.makers.operation.common.domain.Part;


public record AlarmScheduleSendRequest(
        Integer createdGeneration,
        String title,
        String content,
        Category category,
        TargetType targetType,
        List<String> targetList,
        Part part,
        LinkType linkType,
        String link,
        String postDate,
        String postTime
) {
    public Alarm toEntity() {
        return Alarm.builder()
                .createdGeneration(this.createdGeneration)
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .targetType(this.targetType)
                .alarmType(AlarmType.RESERVED)
                .link(this.link)
                .linkType(linkType)
                .part(this.part)
                .status(Status.SCHEDULED)
                .targetList(this.targetList)
                .build();
    }
}

