package org.sopt.makers.operation.app.schedule.service;

import java.time.LocalDateTime;

import org.sopt.makers.operation.app.schedule.dto.response.ScheduleListGetResponse;

public interface ScheduleService {
	ScheduleListGetResponse getSchedules(LocalDateTime startAt, LocalDateTime endAt);
}
