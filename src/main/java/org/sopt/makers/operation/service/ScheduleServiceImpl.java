package org.sopt.makers.operation.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.dto.schedule.SchedulesResponseDTO;
import org.sopt.makers.operation.entity.schedule.Schedule;
import org.sopt.makers.operation.repository.schedule.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	private final ScheduleRepository scheduleRepository;

	@Override
	public SchedulesResponseDTO getSchedules(LocalDateTime start, LocalDateTime end) {
		val schedules = scheduleRepository.findBetweenStartAndEnd(start, end);
		val scheduleMap = classifiedByDate(schedules);
		return SchedulesResponseDTO.of(scheduleMap);
	}

	private Map<LocalDate, List<Schedule>> classifiedByDate(List<Schedule> schedules) {
		val scheduleMap = new HashMap<LocalDate, List<Schedule>>();
		schedules.forEach(schedule -> putScheduleMap(scheduleMap, schedule));
		return scheduleMap;
	}

	private void putScheduleMap(Map<LocalDate, List<Schedule>> scheduleMap, Schedule schedule) {
		val duration = ChronoUnit.DAYS.between(schedule.getStartDate(), schedule.getEndDate());
		scheduleMap.computeIfAbsent(schedule.getStartDate().toLocalDate(), k -> new ArrayList<>()).add(schedule);
		if (duration >= 1) {
			scheduleMap.computeIfAbsent(schedule.getEndDate().toLocalDate(), k -> new ArrayList<>()).add(schedule);
			if (duration >= 2) {
				putScheduleMapBetween(scheduleMap, schedule, (int)duration);
			}
		}
	}

	private void putScheduleMapBetween(Map<LocalDate, List<Schedule>> scheduleMap, Schedule schedule, int duration) {
		for (int i = 1; i < duration; i++) {
			val date = schedule.getStartDate().plusDays(i).toLocalDate();
			scheduleMap.computeIfAbsent(date, k -> new ArrayList<>()).add(schedule);
		}
	}
}
