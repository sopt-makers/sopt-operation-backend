package org.sopt.makers.operation.attendance.repository.attendance;

import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static java.util.Objects.*;
import static org.sopt.makers.operation.attendance.domain.QAttendance.*;
import static org.sopt.makers.operation.attendance.domain.QSubAttendance.*;
import static org.sopt.makers.operation.member.domain.Part.*;
import static org.sopt.makers.operation.lecture.domain.QLecture.*;
import static org.sopt.makers.operation.lecture.domain.QSubLecture.*;
import static org.sopt.makers.operation.member.domain.QMember.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.LectureStatus;
import org.sopt.makers.operation.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceCustomRepository {

	private final JPAQueryFactory queryFactory;
	private final ValueConfig valueConfig;

	@Override
	public List<Attendance> findAttendanceByMemberId(Long memberId) {
		return queryFactory
				.select(attendance)
				.from(attendance)
				.leftJoin(attendance.lecture, lecture)
				.where(attendance.member.id.eq(memberId),
						lecture.lectureStatus.eq(LectureStatus.END),
						lecture.generation.eq(valueConfig.getGENERATION())
				)
				.orderBy(attendance.lecture.startDate.desc())
				.fetch();
	}

	@Override
	public List<Attendance> findFetchJoin(Lecture lecture, Part part, Pageable pageable) {
		return queryFactory
			.selectFrom(attendance)
			.leftJoin(attendance.subAttendances, subAttendance).fetchJoin()
			.leftJoin(subAttendance.subLecture, subLecture).fetchJoin()
			.leftJoin(attendance.member, member).fetchJoin()
			.where(
				attendance.lecture.eq(lecture),
				partEq(part)
			)
			.orderBy(stringTemplate("SUBSTR({0}, 1, 1)", member.name).asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Attendance> findFetchJoin(Member member) {
		return queryFactory
			.selectFrom(attendance)
			.leftJoin(attendance.subAttendances, subAttendance).fetchJoin().distinct()
			.leftJoin(attendance.lecture, lecture).fetchJoin()
			.leftJoin(subAttendance.subLecture, subLecture).fetchJoin()
			.where(attendance.member.eq(member))
			.orderBy(lecture.startDate.desc())
			.fetch();
	}

	@Override
	public List<Attendance> findToday(long memberPlaygroundId) {
		val today = LocalDate.now();
		val startOfDay = today.atStartOfDay();
		val endOfDay = LocalDateTime.of(today, LocalTime.MAX);
		return queryFactory
			.selectFrom(attendance)
			.leftJoin(attendance.lecture, lecture).fetchJoin()
			.leftJoin(attendance.member, member).fetchJoin()
			.leftJoin(attendance.subAttendances, subAttendance).fetchJoin().distinct()
			.leftJoin(subAttendance.subLecture, subLecture).fetchJoin()
			.where(
				member.playgroundId.eq(memberPlaygroundId),
				member.generation.eq(valueConfig.getGENERATION()),
				lecture.part.eq(member.part).or(lecture.part.eq(ALL)),
				lecture.startDate.between(startOfDay, endOfDay))
			.orderBy(lecture.startDate.asc())
			.fetch();
	}

	@Override
	public int count(Lecture lecture, Part part) {
		return Math.toIntExact(queryFactory
			.select(attendance.count())
			.from(attendance)
			.where(
				attendance.lecture.eq(lecture),
					partEq(part)
			)
			.fetchFirst()
		);
	}

	private BooleanExpression partEq(Part part) {
		return (isNull(part) || part.equals(ALL)) ? null : attendance.member.part.eq(part);
	}
}
