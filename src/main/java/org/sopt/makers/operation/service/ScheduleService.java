package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.schedule.SchedulesResponseDTO;

import java.time.LocalDateTime;

public interface ScheduleService {
	SchedulesResponseDTO getSchedules(LocalDateTime start, LocalDateTime end);
}
