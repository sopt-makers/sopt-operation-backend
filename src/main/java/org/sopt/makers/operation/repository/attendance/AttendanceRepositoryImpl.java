package org.sopt.makers.operation.repository.attendance;

import static org.sopt.makers.operation.entity.Part.*;
import static org.sopt.makers.operation.entity.QAttendance.*;
import static org.sopt.makers.operation.entity.QMember.*;
import static org.sopt.makers.operation.entity.QSubAttendance.*;
import static org.sopt.makers.operation.entity.QSubLecture.*;
import static org.sopt.makers.operation.entity.lecture.QLecture.*;

import java.util.List;

import org.sopt.makers.operation.dto.attendance.AttendanceInfo;
import org.sopt.makers.operation.dto.attendance.LectureInfo;
import org.sopt.makers.operation.dto.attendance.MemberInfo;
import org.sopt.makers.operation.dto.attendance.QAttendanceInfo;
import org.sopt.makers.operation.dto.attendance.QLectureInfo;
import org.sopt.makers.operation.dto.attendance.QMemberInfo;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
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

	@Override
	public List<LectureInfo> findLectureAttendances(Lecture lecture, Part part, Pageable pageable) {
		return queryFactory
			.select(new QLectureInfo(
				attendance.id.as("attendanceId"),
				member.id.as("memberId"),
				member.name.as("memberName"),
				member.university.as("university"),
				attendance.lecture.attribute,
				attendance.status.as("attendanceStatus"),
				subAttendance.id.as("subAttendanceId"),
				subLecture.round,
				subAttendance.status.as("subAttendanceStatus"),
				subAttendance.lastModifiedDate.as("updatedAt")
			))
			.from(subAttendance)
			.leftJoin(subAttendance.attendance, attendance)
			.leftJoin(subAttendance.subLecture, subLecture)
			.leftJoin(attendance.member, member)
			.where(
				attendance.lecture.eq(lecture),
				partEq(part)
			)
			.orderBy(member.name.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() * 2L)
			.fetch();
	}

	@Override
	public List<MemberInfo> findByMember(Member member) {
		return queryFactory
			.select(new QMemberInfo(
				lecture.name,
				lecture.attribute,
				attendance.id,
				attendance.status,
				subLecture.round,
				subAttendance.status,
				subAttendance.lastModifiedDate
			))
			.from(subAttendance)
			.leftJoin(subAttendance.attendance, attendance)
			.leftJoin(subAttendance.subLecture, subLecture)
			.leftJoin(subLecture.lecture, lecture)
			.where(attendance.member.eq(member))
			.orderBy(lecture.startDate.asc())
			.fetch();
	}

	@Override
	public List<AttendanceInfo> findAttendancesOfMember(Member member) {
		return queryFactory
			.select(new QAttendanceInfo(
				lecture.attribute,
				attendance.status
			))
			.from(attendance)
			.leftJoin(attendance.lecture, lecture)
			.where(attendance.member.eq(member))
			.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return (part == null || part.equals(ALL)) ? null : member.part.eq(part);
	}
}
