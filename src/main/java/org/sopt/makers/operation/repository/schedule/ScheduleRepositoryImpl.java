package org.sopt.makers.operation.repository.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.entity.schedule.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.sopt.makers.operation.entity.schedule.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Schedule> find(LocalDateTime start, LocalDateTime end) {
		return queryFactory
				.select(schedule)
				.from(schedule)
				.where(schedule.date.between(start, end))
				.fetch();
	}
}
