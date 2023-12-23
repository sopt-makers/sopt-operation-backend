package org.sopt.makers.operation.repository.schedule;

import org.sopt.makers.operation.entity.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleCustomRepository {
    List<Schedule> find(LocalDateTime start, LocalDateTime end);
}
