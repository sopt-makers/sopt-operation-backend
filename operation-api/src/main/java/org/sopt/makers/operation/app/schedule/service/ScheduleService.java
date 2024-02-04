package org.sopt.makers.operation.app.schedule.service;

import java.time.LocalDateTime;

import org.sopt.makers.operation.app.schedule.dto.response.SchedulesResponseDTO;

public interface ScheduleService {
	SchedulesResponseDTO getSchedules(LocalDateTime start, LocalDateTime end);
}
