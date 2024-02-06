package org.sopt.makers.operation.app.schedule.service;

import static org.sopt.makers.operation.code.failure.ScheduleFailureCode.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.sopt.makers.operation.schedule.domain.Schedule;
import org.sopt.makers.operation.schedule.repository.ScheduleRepository;
import org.sopt.makers.operation.exception.ScheduleException;
import org.sopt.makers.operation.app.schedule.dto.response.ScheduleListResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

	private final ScheduleRepository scheduleRepository;

	@Override
	public ScheduleListResponse getSchedules(LocalDateTime start, LocalDateTime end) {
		val scheduleList = scheduleRepository.findBetweenStartAndEnd(start, end);
		val scheduleMap = classifiedByDate(scheduleList, start, end);
		return ScheduleListResponse.of(scheduleMap);
	}

	private Map<LocalDate, List<Schedule>> classifiedByDate(List<Schedule> schedules, LocalDateTime startAt, LocalDateTime endAt) {
		val scheduleMap = initScheduleMap(startAt, endAt);
		schedules.forEach(schedule -> putScheduleMap(scheduleMap, schedule));
		return scheduleMap;
	}

	private Map<LocalDate, List<Schedule>> initScheduleMap(LocalDateTime startAt, LocalDateTime endAt) {
		//TODO: 클라이언트 개발 시간 리소스 절약을 위해 해당 메소드 활용, 가능한 일정이 존재하는 날짜만 key로 가지는 HashMap로 변경 요망
		val duration = getDuration(startAt, endAt);
		return IntStream.range(0, duration)
			.mapToObj(startAt::plusDays)
			.collect(Collectors.toMap(LocalDateTime::toLocalDate, date -> new ArrayList<>()));
	}

	private int getDuration(LocalDateTime startAt, LocalDateTime endAt) {
		val duration = Duration.between(startAt, endAt).toDays() + 1;
		validDuration(duration);
		return (int)duration;
	}

	private void validDuration(long duration) {
		//TODO: 추후 응답 값 형식 변경 후 삭제될 수 있는 메소드
		if (duration <= 0|| duration > 50) {
			throw new ScheduleException(INVALID_DATE_PERM);
		}
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
