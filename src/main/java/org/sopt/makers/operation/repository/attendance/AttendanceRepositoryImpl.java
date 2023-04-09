package org.sopt.makers.operation.repository.attendance;

import static java.util.Objects.*;
import static org.sopt.makers.operation.entity.QAttendance.*;
import static org.sopt.makers.operation.entity.QMember.*;

import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long countAttendance(Lecture lecture) {
		return queryFactory
			.select(attendance.count())
			.from(attendance)
			.where(
				attendance.lecture.eq(lecture),
				attendance.status.eq(AttendanceStatus.ATTENDANCE)
			)
			.fetchOne();
	}

	@Override
	public Long countAbsent(Lecture lecture) {
		return queryFactory
			.select(attendance.count())
			.from(attendance)
			.where(
				attendance.lecture.eq(lecture),
				attendance.status.eq(AttendanceStatus.ABSENT)
			)
			.fetchOne();
	}

	@Override
	public Long countTardy(Lecture lecture) {
		return queryFactory
			.select(attendance.count())
			.from(attendance)
			.where(
				attendance.lecture.eq(lecture),
				attendance.status.eq(AttendanceStatus.TARDY)
			)
			.fetchOne();
	}

	@Override
	public List<Attendance> getAttendanceByPart(Lecture lecture, Part part) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.where(
				attendance.lecture.eq(lecture),
				partEq(part)
			)
			.fetch();
	}

	@Override
	public List<Attendance> findAttendanceByMemberId(Long memberId) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.where(
				attendance.member.id.eq(memberId)
			)
			.orderBy(attendance.lecture.startDate.desc())
			.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return nonNull(part) ? member.part.eq(part) : null;
	}
}
