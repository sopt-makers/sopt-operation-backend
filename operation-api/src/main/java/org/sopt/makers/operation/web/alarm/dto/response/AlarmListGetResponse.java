package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.val;
import org.sopt.makers.operation.alarm.domain.Alarm;

@Builder(access = PRIVATE)
public record AlarmListGetResponse(
        List<AlarmResponse> alarms,
        int totalCount
) {

    public static AlarmListGetResponse of(List<Alarm> alarmList, int totalCount) {
        val alarms = alarmList.stream().map(AlarmResponse::of).toList();
        return AlarmListGetResponse.builder()
                .alarms(alarms)
                .totalCount(totalCount)
                .build();
    }

    @Builder(access = PRIVATE)
    private record AlarmResponse(
            long id,
            String status,
            String sendType,
            String targetType,
            @JsonInclude(value = NON_NULL)
            String targetPart,
            String category,
            String intendAt,
            @JsonInclude(value = NON_NULL)
            String sendAt,
            String title,
            String content
    ) {
        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");

        private static String covertToResponseDateTime(LocalDateTime dateTime) {
            return dateTime.format(DATE_TIME_FORMATTER);
        }
        private static AlarmResponse of(Alarm alarm) {
            return AlarmResponse.builder()
                    .id(alarm.getId())
                    .status(alarm.getStatus().getDescription())
                    .sendType(alarm.getType().getDescription())
                    .targetType(alarm.getTarget().getTargetType().getName())
                    .targetPart(alarm.getTarget().getTargetPart().getName())
                    .category(alarm.getContent().getCategory().getName())
                    .intendAt(covertToResponseDateTime(alarm.getIntendedAt()))
                    .sendAt(covertToResponseDateTime(alarm.getSendAt()))
                    .title(alarm.getContent().getTitle())
                    .content(alarm.getContent().getContent())
                    .build();
        }

    }
}
