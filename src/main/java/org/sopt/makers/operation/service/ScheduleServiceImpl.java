package org.sopt.makers.operation.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.dto.schedule.SchedulesResponseDTO;
import org.sopt.makers.operation.entity.schedule.Schedule;
import org.sopt.makers.operation.repository.schedule.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	private final ScheduleRepository scheduleRepository;
	public Map<LocalDateTime, List<Schedule>> createCalendar(LocalDateTime start, LocalDateTime end) {
		long days = Duration.between(start, end).toDays() + 1;

		return LongStream.range(0, days)
				.mapToObj(start::plusDays)
				.collect(Collectors.toMap(
						date -> date,
						date -> new ArrayList<>()
				));
	}
	@Override
	public SchedulesResponseDTO getSchedules(LocalDateTime start, LocalDateTime end) {
		val calendar = createCalendar(start, end);
		val schedules = scheduleRepository.find(start, end);

		schedules.forEach(schedule -> {
			val scheduleList = calendar.get(schedule.getDate());
			scheduleList.add(schedule);
			calendar.put(schedule.getDate(), scheduleList);
		});

		return SchedulesResponseDTO.of(calendar);
	}
}
