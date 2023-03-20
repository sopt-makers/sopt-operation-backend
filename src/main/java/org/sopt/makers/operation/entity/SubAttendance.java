package org.sopt.makers.operation.entity;

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

	@Id @GeneratedValue
	@Column(name = "sub_attendance_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendance_id")
	private Attendance attendance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_session_id")
	private SubSession subSession;

	private boolean isCheck;

	public SubAttendance(Attendance attendance, SubSession subSession) {
		this.attendance = attendance;
		this.subSession = subSession;
		isCheck = false;
	}
}
