package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;
import static org.sopt.makers.operation.constant.AlarmConstant.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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
            @JsonInclude(value = NON_NULL)
            String intendAt,
            String sendAt,
            String title,
            String content
    ) {
        private static final String DATETIME_FORMAT = String.join(" ", ALARM_RESPONSE_DATE_FORMAT, ALARM_RESPONSE_TIME_FORMAT);

        private static String covertToResponseDateTime(LocalDateTime dateTime) {
            if (Objects.isNull(dateTime)){
                return null;
            }
            return dateTime.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
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
