package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SubAttendance extends BaseEntity {

	@Id @GeneratedValue(strategy = IDENTITY)
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
		status = AttendanceStatus.ABSENT;
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
}
