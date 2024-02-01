package org.sopt.makers.operation.repository.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.operation.schedule.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.operation.schedule.QSchedule.schedule;

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
