package org.sopt.makers.operation.repository.lecture;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.config.GenerationConfig;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.*;
import static org.sopt.makers.operation.entity.lecture.QLecture.*;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final GenerationConfig generationConfig;
    private final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Override
    public List<Lecture> searchLecture(LectureSearchCondition lectureSearchCondition) {
        val now = LocalDateTime.now(KST);
        val today = now.toLocalDate();
        val startOfDay = today.atStartOfDay();
        val endOfDay = LocalDateTime.of(today, LocalTime.MAX);

        List<Part> partList = Arrays.asList(lectureSearchCondition.part(), Part.ALL);

        return queryFactory
                .selectFrom(lecture)
                .where(
                        lecture.part.in(partList),
                        lecture.generation.eq(generationConfig.getCurrentGeneration()),
                        lecture.startDate.between(startOfDay, endOfDay)
                )
                .orderBy(lecture.startDate.asc())
                .fetch();
    }

    @Override
    public List<Lecture> findLectures(int generation, Part part) {
        return queryFactory
            .selectFrom(lecture)
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
