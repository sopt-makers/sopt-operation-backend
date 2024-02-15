package org.sopt.makers.operation.schedule.repository;

import static org.sopt.makers.operation.schedule.domain.QSchedule.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.schedule.domain.Schedule;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Schedule> findBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return queryFactory
			.select(schedule)
			.from(schedule)
			.where(
					schedule.startDate.eq(startDate)
							.or(schedule.startDate.between(startDate, endDate))
							.or(schedule.startDate.eq(endDate))
							.or(schedule.endDate.eq(startDate))
							.or(schedule.endDate.between(startDate, endDate))
							.or(schedule.endDate.eq(endDate))
			)
			.orderBy(schedule.startDate.asc())
			.fetch();
	}
}
