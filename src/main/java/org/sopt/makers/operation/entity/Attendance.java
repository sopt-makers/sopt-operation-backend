package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sopt.makers.operation.entity.lecture.Lecture;

import lombok.*;

@Entity
@NoArgsConstructor
@Getter
public class Attendance {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "attendance_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id")
	private Lecture lecture;

	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;

	@OneToMany(mappedBy = "attendance")
	private final List<SubAttendance> subAttendances = new ArrayList<>();

	public Attendance(Member member, Lecture lecture) {
		setMember(member);
		setLecture(lecture);
		this.status = AttendanceStatus.ABSENT;
	}

	public void updateStatus(AttendanceStatus status) {
		this.status = status;
	}

	public void updateStatus() {
		this.status = getStatus();
	}

	public AttendanceStatus getStatus() {
		val first = getSubAttendanceByRound(1);
		val second = getSubAttendanceByRound(2);
		return switch (this.lecture.getAttribute()) {
			case SEMINAR -> second.getStatus().equals(ATTENDANCE)
				? first.getStatus().equals(ATTENDANCE) ? ATTENDANCE : TARDY
				: ABSENT;
			case EVENT -> second.getStatus().equals(ATTENDANCE) ? ATTENDANCE : ABSENT;
			case ETC -> second.getStatus().equals(ATTENDANCE) ? PARTICIPATE : NOT_PARTICIPATE;
		};
	}

	private SubAttendance getSubAttendanceByRound(int round) {
		return this.subAttendances.stream().filter(o -> o.getSubLecture().getRound() == round).findFirst()
			.orElseThrow(() -> new EntityNotFoundException(INVALID_SUB_ATTENDANCE.getName()));
	}

	private void setMember(Member member) {
		if (Objects.nonNull(this.member)) {
			this.member.getAttendances().remove(this);
		}
		this.member = member;
		member.getAttendances().add(this);
	}

	private void setLecture(Lecture lecture) {
		if (Objects.nonNull(this.lecture)) {
			this.lecture.getAttendances().remove(this);
		}
		this.lecture = lecture;
		lecture.getAttendances().add(this);
	}
}
