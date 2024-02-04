package org.sopt.makers.operation.service.app.schedule.dto.response;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.sopt.makers.operation.domain.lecture.Attribute;
import org.sopt.makers.operation.domain.schedule.domain.Schedule;

import lombok.Builder;

public record SchedulesResponseDTO(
    List<DateVO> dates
) {
    public static SchedulesResponseDTO of(Map<LocalDate, List<Schedule>> scheduleMap) {
        return new SchedulesResponseDTO(
            scheduleMap.keySet().stream().sorted()
                .map(key -> DateVO.of(key, scheduleMap.get(key)))
                .toList()
        );
    }

    @Builder
    record DateVO(
        String date,
        String dayOfWeek,
        List<ScheduleVO> schedules
    ) {
        static DateVO of(LocalDate date, List<Schedule> schedules) {
            return DateVO.builder()
                .date(date.toString())
                .dayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                .schedules(schedules.stream().map(ScheduleVO::of).toList())
                .build();
        }
    }

    @Builder
    record ScheduleVO(
            Long scheduleId,
            String startDate,
            String endDate,
            Attribute attribute,
            String title
    ) {
        static ScheduleVO of(Schedule schedule) {
            return ScheduleVO.builder()
                    .scheduleId(schedule.getId())
                    .startDate(schedule.getStartDate().toString())
                    .endDate(schedule.getEndDate().toString())
                    .attribute(schedule.getAttribute())
                    .title(schedule.getTitle())
                    .build();
        }
    }
}
