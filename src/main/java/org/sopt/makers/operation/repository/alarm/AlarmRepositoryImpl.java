package org.sopt.makers.operation.repository.alarm;

import static java.util.Objects.*;
import static org.sopt.makers.operation.entity.Part.*;
import static org.sopt.makers.operation.entity.alarm.QAlarm.*;

import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Alarm;
import org.sopt.makers.operation.entity.alarm.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Alarm> getAlarms(Integer generation, Part part, Status status, Pageable pageable) {
		return queryFactory
			.selectFrom(alarm)
			.where(
				generationEq(generation),
				partEq(part),
				statusEq(status)
			)
			.orderBy(alarm.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private BooleanExpression generationEq(Integer generation) {
		return nonNull(generation) ? alarm.generation.eq(generation) : null;
	}

	private BooleanExpression partEq(Part part) {
		return (nonNull(part) && !part.equals(ALL)) ? alarm.part.eq(part) : null;
	}

	private BooleanExpression statusEq(Status status) {
		return nonNull(status) ? alarm.status.eq(status) : null;
	}
}
