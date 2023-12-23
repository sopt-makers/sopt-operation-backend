package org.sopt.makers.operation.dto.schedule;

import lombok.Builder;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SchedulesResponseDTO(
        Map<LocalDateTime, List<ScheduleVO>> calendar
) {

    public static SchedulesResponseDTO of(Map<LocalDateTime, List<Schedule>> calendar) {
        Map<LocalDateTime, List<ScheduleVO>> convertedCalendar = calendar.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(ScheduleVO::of).collect(Collectors.toList())
                ));
        return new SchedulesResponseDTO(convertedCalendar);
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
