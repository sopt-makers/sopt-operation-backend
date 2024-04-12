package org.sopt.makers.operation.attendance.domain;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

import java.util.Objects;

import org.sopt.makers.operation.common.domain.BaseEntity;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SubAttendance extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sub_attendance_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendance_id")
	private Attendance attendance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_lecture_id")
	private SubLecture subLecture;

	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;

	public SubAttendance(Attendance attendance, SubLecture subLecture) {
		setAttendance(attendance);
		setSubLecture(subLecture);
		status = ABSENT;
	}

	protected SubAttendance(Long id, Attendance attendance, SubLecture subLecture, AttendanceStatus status) {
		this.id = id;
		setAttendance(attendance);
		setSubLecture(subLecture);
		this.status = status;
	}

	private void setAttendance(Attendance attendance) {
		if (Objects.nonNull(this.attendance)) {
			this.attendance.getSubAttendances().remove(this);
		}
		this.attendance = attendance;
		attendance.getSubAttendances().add(this);
	}

	private void setSubLecture(SubLecture subLecture) {
		if (Objects.nonNull(this.subLecture)) {
			this.subLecture.getSubAttendances().remove(this);
		}
		this.subLecture = subLecture;
		subLecture.getSubAttendances().add(this);
	}

	public void updateStatus(AttendanceStatus status) {
		this.status = status;
		this.attendance.updateStatus();
	}

	public boolean isMatchRound(int round) {
		return this.subLecture.getRound() == round;
	}
}
