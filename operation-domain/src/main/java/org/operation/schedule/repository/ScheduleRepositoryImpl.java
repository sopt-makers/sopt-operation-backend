package org.operation.schedule.repository;

import static org.operation.schedule.QSchedule.*;

import java.time.LocalDateTime;
import java.util.List;

import org.operation.schedule.domain.Schedule;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Schedule> findBetweenStartAndEnd(LocalDateTime start, LocalDateTime end) {
		return queryFactory
			.select(schedule)
			.from(schedule)
			.where(
				schedule.startDate.between(start, end)
					.or(schedule.endDate.between(start, end))
			)
			.orderBy(schedule.startDate.asc())
			.fetch();
	}
}
