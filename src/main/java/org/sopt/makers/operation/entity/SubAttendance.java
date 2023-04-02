package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class SubAttendance {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "sub_attendance_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendance_id")
	private Attendance attendance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_lecture_id")
	private SubLecture subLecture;

	private boolean isCheck;

	public SubAttendance(Attendance attendance, SubLecture subLecture) {
		this.attendance = attendance;
		this.subLecture = subLecture;
		isCheck = false;
	}
}
