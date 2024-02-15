package org.sopt.makers.operation.app.schedule.dto.response;

import static java.time.format.TextStyle.*;
import static java.util.Locale.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.schedule.domain.Schedule;

import lombok.Builder;

@Builder(access = PRIVATE)
public record ScheduleListGetResponse(
    List<DateResponse> dates
) {

    public static ScheduleListGetResponse of(Map<LocalDate, List<Schedule>> scheduleMap) {
        return ScheduleListGetResponse.builder()
                .dates(getDates(scheduleMap))
                .build();
    }

    private static List<DateResponse> getDates(Map<LocalDate, List<Schedule>> scheduleMap) {
        return scheduleMap.keySet().stream().sorted()
                .map(key -> DateResponse.of(key, scheduleMap.get(key)))
                .toList();
    }

    @Builder(access = PRIVATE)
    record DateResponse(
        String date,
        String dayOfWeek,
        List<ScheduleResponse> schedules
    ) {

        private static DateResponse of(LocalDate date, List<Schedule> schedules) {
            return DateResponse.builder()
                .date(date.toString())
                .dayOfWeek(date.getDayOfWeek().getDisplayName(SHORT, KOREAN))
                .schedules(schedules.stream().map(ScheduleResponse::of).toList())
                .build();
        }
    }

    @Builder(access = PRIVATE)
    record ScheduleResponse(
            long scheduleId,
            String startDate,
            String endDate,
            Attribute attribute,
            String title
    ) {

        private static ScheduleResponse of(Schedule schedule) {
            return ScheduleResponse.builder()
                    .scheduleId(schedule.getId())
                    .startDate(schedule.getStartDate().toString())
                    .endDate(schedule.getEndDate().toString())
                    .attribute(schedule.getAttribute())
                    .title(schedule.getTitle())
                    .build();
        }
    }
}
