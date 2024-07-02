package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.Category;
import org.sopt.makers.operation.alarm.domain.LinkType;
import org.sopt.makers.operation.alarm.domain.TargetType;
import org.sopt.makers.operation.common.domain.Part;

@Builder(access = PRIVATE)
public record AlarmGetResponse(
        Category category,
        @JsonInclude(value = NON_NULL)
        String part,
        TargetType targetType,
        String title,
        String content,
        String link,
        LinkType linkType,
        String createdAt,
        @JsonInclude(value = NON_NULL)
        String sendAt
) {

    public static AlarmGetResponse of(Alarm alarm) {
        return AlarmGetResponse.builder()
                .category(alarm.getCategory())
                .part(getPartName(alarm.getPart()))
                .targetType(alarm.getTargetType())
                .title(alarm.getTitle())
                .content(alarm.getContent())
                .link(alarm.getLink())
                .linkType(alarm.getLinkType())
                .createdAt(alarm.getCreatedDate().toString())
                .sendAt(alarm.getSendAt())
                .build();
    }

    private static String getPartName(Part part) {
        return nonNull(part) ? part.getName() : null;
    }

}
