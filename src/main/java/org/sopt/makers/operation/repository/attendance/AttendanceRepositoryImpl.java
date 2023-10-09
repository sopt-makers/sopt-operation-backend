package org.sopt.makers.operation.repository.attendance;

import static org.sopt.makers.operation.entity.Part.*;
import static org.sopt.makers.operation.entity.QAttendance.*;
import static org.sopt.makers.operation.entity.QMember.*;
import static org.sopt.makers.operation.entity.QSubAttendance.*;
import static org.sopt.makers.operation.entity.QSubLecture.*;
import static org.sopt.makers.operation.entity.lecture.QLecture.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.val;

import org.sopt.makers.operation.config.GenerationConfig;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.entity.lecture.LectureStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceCustomRepository {

	private final JPAQueryFactory queryFactory;
	private final GenerationConfig generationConfig;

	@Override
	public List<Attendance> findAttendanceByMemberId(Long memberId) {

		return queryFactory
				.select(attendance)
				.from(attendance)
				.leftJoin(attendance.lecture, lecture)
				.where(attendance.member.id.eq(memberId),
						lecture.lectureStatus.eq(LectureStatus.END),
						lecture.generation.eq(generationConfig.getCurrentGeneration())
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
	public List<Attendance> findByLecture(Lecture lecture) {
		return queryFactory
			.select(attendance)
			.from(attendance)
			.leftJoin(attendance.member, member).fetchJoin().distinct()
			.where(attendance.lecture.eq(lecture))
			.fetch();
	}

	@Override
	public List<Attendance> findCurrentAttendanceByMember(Long playGroundId) {
		val now = LocalDateTime.now();
		val today = now.toLocalDate();
		val startOfDay = today.atStartOfDay();
		val endOfDay = LocalDateTime.of(today, LocalTime.MAX);

		return queryFactory
			.select(attendance)
			.from(attendance)
			.leftJoin(attendance.lecture, lecture).fetchJoin()
			.leftJoin(attendance.member, member).fetchJoin()
			.where(
				lecture.part.eq(member.part).or(lecture.part.eq(Part.ALL)),
				lecture.startDate.between(startOfDay, endOfDay),
				member.playgroundId.eq(playGroundId),
				member.generation.eq(generationConfig.getCurrentGeneration())
			)
			.orderBy(lecture.startDate.asc())
			.fetch();
	}

	@Override
	public List<SubAttendance> findSubAttendanceByAttendanceId(Long attendanceId) {
		return queryFactory
			.select(subAttendance)
			.from(subAttendance)
			.leftJoin(subAttendance.subLecture, subLecture).fetchJoin()
			.where(
				subAttendance.attendance.id.eq(attendanceId)
			)
			.orderBy(subAttendance.createdDate.asc())
			.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return (part == null || part.equals(ALL)) ? null : member.part.eq(part);
	}
}
