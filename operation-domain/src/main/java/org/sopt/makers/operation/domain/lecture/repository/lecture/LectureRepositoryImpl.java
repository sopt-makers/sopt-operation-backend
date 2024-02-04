package org.sopt.makers.operation.domain.lecture.repository.lecture;

import static java.util.Objects.*;
import static org.sopt.makers.operation.domain.attendance.domain.QAttendance.*;
import static org.sopt.makers.operation.domain.lecture.LectureStatus.*;
import static org.sopt.makers.operation.domain.lecture.QLecture.*;
import static org.sopt.makers.operation.domain.member.domain.QMember.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.lecture.Lecture;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureCustomRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Lecture> find(int generation, Part part) {
        return queryFactory
            .selectFrom(lecture)
            .leftJoin(lecture.attendances, attendance).fetchJoin().distinct()
            .where(
                lecture.generation.eq(generation),
                partEq(part)
            )
            .orderBy(lecture.startDate.desc())
            .fetch();
    }

    @Override
    public List<Lecture> findLecturesToBeEnd() {
        return queryFactory
            .selectFrom(lecture)
            .leftJoin(lecture.attendances, attendance).fetchJoin().distinct()
            .leftJoin(attendance.member, member).fetchJoin().distinct()
            .where(
                lecture.endDate.before(LocalDateTime.now()),
                lecture.lectureStatus.ne(END)
            )
            .fetch();
    }

    @Override
    public Optional<Lecture> find(Long lectureId) {
        return queryFactory
            .selectFrom(lecture)
            .leftJoin(lecture.attendances, attendance).fetchJoin().distinct()
            .leftJoin(attendance.member, member).fetchJoin().distinct()
            .where(lecture.id.eq(lectureId))
            .stream().findFirst();
    }

    private BooleanExpression partEq(Part part) {
        return nonNull(part) ? lecture.part.eq(part) : null;
    }

}
