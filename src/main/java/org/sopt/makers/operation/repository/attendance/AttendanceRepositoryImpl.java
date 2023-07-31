package org.sopt.makers.operation.repository.attendance;

import static org.sopt.makers.operation.entity.Part.*;
import static org.sopt.makers.operation.entity.QAttendance.*;
import static org.sopt.makers.operation.entity.QMember.*;
import static org.sopt.makers.operation.entity.QSubAttendance.*;
import static org.sopt.makers.operation.entity.QSubLecture.*;
import static org.sopt.makers.operation.entity.lecture.QLecture.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import lombok.val;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.QSubAttendance;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceCustomRepository {

	private final JPAQueryFactory queryFactory;
	private final ZoneId KST = ZoneId.of("Asia/Seoul");

	@Override
	public List<Attendance> findAttendanceByMemberId(Long memberId) {
		val now = LocalDateTime.now(KST);

		return queryFactory
				.select(attendance)
				.from(attendance)
				.leftJoin(attendance.lecture, lecture)
				.where(attendance.member.id.eq(memberId),
						lecture.endDate.before(now)
				)
				.orderBy(attendance.lecture.startDate.desc())
				.fetch();
	}

	@Override
	public List<Attendance> findAttendancesByLecture(Long lectureId, Part part, Pageable pageable) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.join(attendance.subAttendances, subAttendance).fetchJoin()
			.join(subAttendance.subLecture, subLecture).fetchJoin()
			.join(attendance.lecture, lecture).fetchJoin()
			.join(attendance.member, member).fetchJoin().distinct()
			.where(
				attendance.lecture.id.eq(lectureId),
				partEq(part)
			)
			.orderBy(member.name.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Attendance> findAttendancesByMember(Long memberId) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.leftJoin(attendance.subAttendances, subAttendance).fetchJoin().distinct()
			.leftJoin(attendance.lecture, lecture).fetchJoin()
			.leftJoin(subAttendance.subLecture, subLecture).fetchJoin()
			.where(attendance.member.id.eq(memberId))
			.orderBy(lecture.startDate.desc())
			.fetch();
	}

	@Override
	public Optional<Attendance> findAttendanceBySubAttendance(SubAttendance subAttendance) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.join(attendance.subAttendances, QSubAttendance.subAttendance).fetchJoin()
			.join(QSubAttendance.subAttendance.subLecture, subLecture).fetchJoin()
			.where(attendance.subAttendances.contains(subAttendance))
			.stream().findFirst();
	}

	@Override
	public List<Attendance> findByLecture(Lecture lecture) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.leftJoin(attendance.member, member).fetchJoin().distinct()
			.where(attendance.lecture.eq(lecture))
			.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return (part == null || part.equals(ALL)) ? null : member.part.eq(part);
	}
}
