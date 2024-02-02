package org.operation.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.operation.schedule.domain.Schedule;

public interface ScheduleCustomRepository {
    List<Schedule> findBetweenStartAndEnd(LocalDateTime start, LocalDateTime end);
}
