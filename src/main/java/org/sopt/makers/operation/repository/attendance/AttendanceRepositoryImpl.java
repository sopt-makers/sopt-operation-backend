package org.sopt.makers.operation.repository.attendance;

import static com.querydsl.core.types.dsl.Expressions.*;
import static java.util.Objects.*;
import static org.operation.attendance.QAttendance.*;
import static org.operation.attendance.QSubAttendance.*;
import static org.operation.lecture.QLecture.*;
import static org.operation.lecture.QSubLecture.*;
import static org.operation.member.QMember.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.val;

import org.sopt.makers.operation.config.ValueConfig;
import org.operation.attendance.Attendance;
import org.operation.member.Member;
import org.sopt.makers.operation.entity.Part;
import org.operation.lecture.LectureStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
	public List<Attendance> findByLecture(Long lectureId, Part part, Pageable pageable) {
		return queryFactory
			.selectFrom(attendance)
			.leftJoin(attendance.subAttendances, subAttendance).fetchJoin()
			.leftJoin(subAttendance.subLecture, subLecture).fetchJoin()
			.leftJoin(attendance.lecture, lecture).fetchJoin()
			.leftJoin(attendance.member, member).fetchJoin()
			.where(
				attendance.lecture.id.eq(lectureId),
				partEq(part)
			)
			.orderBy(stringTemplate("SUBSTR({0}, 1, 1)", member.name).asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Attendance> findByMember(Member member) {
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
				lecture.part.eq(member.part).or(lecture.part.eq(Part.ALL)),
				lecture.startDate.between(startOfDay, endOfDay))
			.orderBy(lecture.startDate.asc())
			.fetch();
	}

	@Override
	public int countByLectureIdAndPart(long lectureId, Part part) {
		return Math.toIntExact(queryFactory
			.select(attendance.count())
			.from(attendance)
			.where(
				attendance.lecture.id.eq(lectureId),
				nonNull(part) ? attendance.member.part.eq(part) : null
			)
			.fetchFirst()
		);
	}

	private BooleanExpression partEq(Part part) {
		return nonNull(part) ? member.part.eq(part) : null;
	}
}
