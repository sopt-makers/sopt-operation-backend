package org.sopt.makers.operation.repository.lecture;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.Arrays;
import java.util.List;

import static org.sopt.makers.operation.entity.lecture.QLecture.*;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Lecture> searchLecture(LectureSearchCondition lectureSearchCondition) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX);

        List<Part> partList = Arrays.asList(lectureSearchCondition.part(), Part.ALL);

        return queryFactory
                .selectFrom(lecture)
                .where(
                        lecture.part.in(partList),
                        lecture.generation.eq(32),
                        lecture.startDate.between(startOfDay, endOfDay)
                )
                .fetch();
    }

}
