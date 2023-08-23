package org.sopt.makers.operation.repository.lecture;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.config.GenerationConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Objects.*;
import static org.sopt.makers.operation.entity.QAttendance.*;
import static org.sopt.makers.operation.entity.lecture.QLecture.*;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final GenerationConfig generationConfig;
    @Override
    public List<Lecture> findLectures(int generation, Part part) {
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

    private BooleanExpression partEq(Part part) {
        return nonNull(part) ? lecture.part.eq(part) : null;
    }

}
