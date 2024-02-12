package org.sopt.makers.operation.app.schedule.service;

import java.time.LocalDateTime;

import org.sopt.makers.operation.app.schedule.dto.response.ScheduleListResponse;

public interface ScheduleService {
	ScheduleListResponse getSchedules(LocalDateTime start, LocalDateTime end);
}
