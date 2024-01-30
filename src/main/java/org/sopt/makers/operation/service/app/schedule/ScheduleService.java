package org.sopt.makers.operation.service.app.schedule;

import java.time.LocalDateTime;

import org.sopt.makers.operation.dto.schedule.response.SchedulesResponseDTO;

public interface ScheduleService {
	SchedulesResponseDTO getSchedules(LocalDateTime start, LocalDateTime end);
}
