package org.sopt.makers.operation.repository.schedule;

import org.operation.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleCustomRepository {
    List<Schedule> findBetweenStartAndEnd(LocalDateTime start, LocalDateTime end);
}
