package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Attendance {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "attendance_id")
	private Long id;

	private Long memberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id")
	private Session session;

	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;

	@OneToMany(mappedBy = "attendance")
	private List<SubAttendance> subAttendances = new ArrayList<>();

	public Attendance(Long memberId, Session session) {
		this.memberId = memberId;
		this.session = session;
		this.status = AttendanceStatus.ABSENT;
	}
}
