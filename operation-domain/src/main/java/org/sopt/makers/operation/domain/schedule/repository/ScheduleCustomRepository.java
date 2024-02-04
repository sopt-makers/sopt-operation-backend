package org.sopt.makers.operation.domain.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.domain.schedule.domain.Schedule;

public interface ScheduleCustomRepository {
    List<Schedule> findBetweenStartAndEnd(LocalDateTime start, LocalDateTime end);
}
