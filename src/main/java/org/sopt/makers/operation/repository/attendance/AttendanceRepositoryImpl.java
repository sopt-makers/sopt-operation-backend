package org.sopt.makers.operation.repository.attendance;

import static org.sopt.makers.operation.entity.QAttendance.*;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.stereotype.Repository;

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
}
