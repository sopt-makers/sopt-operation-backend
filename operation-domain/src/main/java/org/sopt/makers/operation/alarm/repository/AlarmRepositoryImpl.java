package org.sopt.makers.operation.alarm.repository;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.alarm.domain.QAlarm.alarm;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Alarm> findOrderByCreatedDate(Integer generation, AlarmStatus status, Pageable pageable) {
        return queryFactory
                .selectFrom(alarm)
                .where(
                        generationEq(generation),
                        statusEq(status)
                )
                .orderBy(alarm.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public int count(Integer generation, AlarmStatus status) {
        return Math.toIntExact(queryFactory
                .select(alarm.count())
                .from(alarm)
                .where(
                        generationEq(generation),
                        statusEq(status)
                )
                .fetchFirst());
    }

    private BooleanExpression generationEq(Integer generation) {
        return nonNull(generation) ? alarm.target.generation.eq(generation) : null;
    }

    private BooleanExpression statusEq(AlarmStatus status) {
        return nonNull(status) ? alarm.status.eq(status) : null;
    }
}
