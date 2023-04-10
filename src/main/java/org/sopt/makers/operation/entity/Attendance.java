package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;

import org.sopt.makers.operation.entity.lecture.Lecture;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
