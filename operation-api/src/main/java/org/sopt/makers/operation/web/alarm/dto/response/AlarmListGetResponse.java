package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.common.domain.Part;

@Builder(access = PRIVATE)
public record AlarmListGetResponse(
        List<AlarmResponse> alarms,
        int totalCount
) {

    public static AlarmListGetResponse of(List<Alarm> alarmList, int totalCount) {
        return AlarmListGetResponse.builder()
                .alarms(alarmList.stream().map(AlarmResponse::of).toList())
                .totalCount(totalCount)
                .build();
    }

    @Builder(access = PRIVATE)
    private record AlarmResponse(
            long alarmId,
            @JsonInclude(value = NON_NULL)
            String part,
            String category,
            String title,
            String content,
            @JsonInclude(value = NON_NULL)
            String sendAt,
            String alarmType,
            Status status
    ) {

        private static AlarmResponse of(Alarm alarm) {
            return AlarmResponse.builder()
                    .alarmId(alarm.getId())
                    .part(getPartName(alarm.getPart()))
                    .category(alarm.getCategory().getName())
                    .title(alarm.getTitle())
                    .content(alarm.getContent())
                    .sendAt(alarm.getSendAt())
                    .alarmType(alarm.getAlarmType().getDescription())
                    .status(alarm.getStatus())
                    .build();
        }

        private static String getPartName(Part part) {
            return nonNull(part) ? part.getName() : null;
        }

    }
}
