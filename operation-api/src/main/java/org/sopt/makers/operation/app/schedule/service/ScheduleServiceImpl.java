package org.sopt.makers.operation.app.schedule.service;

import static java.time.temporal.ChronoUnit.*;
import static org.sopt.makers.operation.code.failure.ScheduleFailureCode.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.schedule.domain.Schedule;
import org.sopt.makers.operation.schedule.repository.ScheduleRepository;
import org.sopt.makers.operation.exception.ScheduleException;
import org.sopt.makers.operation.app.schedule.dto.response.ScheduleListGetResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

	private final ScheduleRepository scheduleRepository;

	private final ValueConfig valueConfig;

	@Override
	public ScheduleListGetResponse getSchedules(LocalDateTime startAt, LocalDateTime endAt) {
		val schedules = scheduleRepository.findBetween(startAt, endAt);
		val scheduleMap = getClassifiedMap(schedules, startAt, endAt);
		return ScheduleListGetResponse.of(scheduleMap);
	}

	private Map<LocalDate, List<Schedule>> getClassifiedMap(
			List<Schedule> schedules,
			LocalDateTime startAt,
			LocalDateTime endAt
	) {
		val scheduleMap = getInitializedMap(startAt, endAt);
		schedules.forEach(schedule -> putScheduleToMap(scheduleMap, schedule));
		return scheduleMap;
	}

	private Map<LocalDate, List<Schedule>> getInitializedMap(LocalDateTime startAt, LocalDateTime endAt) {
		//TODO: 클라이언트 개발 시간 리소스 절약을 위해 해당 메소드 활용, 가능한 일정이 존재하는 날짜만 key로 가지는 HashMap로 변경 요망
		val duration = getDuration(startAt, endAt);
		return IntStream.range(0, duration)
			.mapToObj(startAt::plusDays)
			.collect(Collectors.toMap(LocalDateTime::toLocalDate, date -> new ArrayList<>()));
	}

	private int getDuration(LocalDateTime startAt, LocalDateTime endAt) {
		val duration = Duration.between(startAt, endAt).toDays() + 1;
		val minDuration = valueConfig.getMIN_SCHEDULE_DURATION();
		val maxDuration = valueConfig.getMAX_SCHEDULE_DURATION();

		if (duration < minDuration || duration > maxDuration) {
			throw new ScheduleException(INVALID_DATE_PERM);
		}

		return (int)duration;
	}

	private void putScheduleToMap(Map<LocalDate, List<Schedule>> scheduleMap, Schedule schedule) {
		val duration = DAYS.between(schedule.getStartDate(), schedule.getEndDate());
		val dayDuration = valueConfig.getDAY_DURATION();
		val twoDaysDuration = valueConfig.getTWO_DAYS_DURATION();

		scheduleMap.computeIfAbsent(schedule.getStartDate().toLocalDate(), k -> new ArrayList<>()).add(schedule);

		if (duration >= dayDuration) {
			scheduleMap.computeIfAbsent(schedule.getEndDate().toLocalDate(), k -> new ArrayList<>()).add(schedule);
			if (duration >= twoDaysDuration) {
				putScheduleMapBetween(scheduleMap, schedule, (int)duration);
			}
		}
	}

	private void putScheduleMapBetween(Map<LocalDate, List<Schedule>> scheduleMap, Schedule schedule, int duration) {
		Stream.iterate(1, i -> i + 1).limit(duration - 1)
				.forEach(i -> putScheduleAtDayCount(scheduleMap, schedule, i));
	}

	private void putScheduleAtDayCount(Map<LocalDate, List<Schedule>> scheduleMap, Schedule schedule, int dayCount) {
		val date = schedule.getStartDate().plusDays(dayCount).toLocalDate();
		scheduleMap.computeIfAbsent(date, k -> new ArrayList<>()).add(schedule);
	}
}
