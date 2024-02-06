package org.sopt.makers.operation.attendance.domain;

import static javax.persistence.GenerationType.*;
import static org.sopt.makers.operation.code.failure.AttendanceFailureCode.*;
import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

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

import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.exception.AttendanceException;

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
		this.status = ABSENT;
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

	public float getScore() {
		val lectureAttribute = this.lecture.getAttribute();
		return switch (lectureAttribute) {
			case SEMINAR -> {
				if (this.status.equals(ABSENT)) {
					yield  -1f;
				} else if (this.status.equals(TARDY)) {
					yield -0.5f;
				} else {
					yield 0f;
				}
			}
			case EVENT -> this.status.equals(ATTENDANCE) ? 0.5f : 0f;
			default -> 0f;
		};
	}

	public void updateMemberScore() {
		this.member.updateScore(this.getScore());
	}

	public void restoreMemberScore() {
		this.member.updateScore((-1) * this.getScore());
	}

	private SubAttendance getSubAttendanceByRound(int round) {
		return this.subAttendances.stream().filter(o -> o.getSubLecture().getRound() == round).findFirst()
			.orElseThrow(() -> new AttendanceException(INVALID_SUB_ATTENDANCE));
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

	public boolean isEnd() {
		return this.lecture.isEnd();
	}
}
