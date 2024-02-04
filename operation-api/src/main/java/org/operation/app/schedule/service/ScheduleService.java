package org.operation.app.schedule.service;

import java.time.LocalDateTime;

import org.operation.app.schedule.dto.response.SchedulesResponseDTO;

public interface ScheduleService {
	SchedulesResponseDTO getSchedules(LocalDateTime start, LocalDateTime end);
}
