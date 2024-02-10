package org.sopt.makers.operation.lecture.domain;

import static java.util.Objects.*;
import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sopt.makers.operation.attendance.domain.SubAttendance;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SubLecture {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "sub_lecture_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id")
	private Lecture lecture;
	private int round;

	private LocalDateTime startAt;

	private String code;

	@OneToMany(mappedBy = "subLecture")
	private final List<SubAttendance> subAttendances = new ArrayList<>();

	public SubLecture(Lecture lecture, int round) {
		setLecture(lecture);
		this.round = round;
	}

	public void startAttendance(String code) {
		this.startAt = LocalDateTime.now();
		this.code = code;
		this.lecture.updateStatus(getUpdatedStatus());
	}

	private LectureStatus getUpdatedStatus() {
		return switch (this.round) {
			case 1 -> LectureStatus.FIRST;
			case 2 -> LectureStatus.SECOND;
			default -> this.lecture.getLectureStatus();
		};
	}

	private void setLecture(Lecture lecture) {
		if (Objects.nonNull(this.lecture)) {
			this.lecture.getSubLectures().remove(this);
		}
		this.lecture = lecture;
		lecture.getSubLectures().add(this);
	}

	public boolean isNotStarted() {
		return isNull(this.startAt) || isNull(this.code) || this.startAt.isAfter(LocalDateTime.now());
	}

	public boolean isEnded(int attendanceMinute) {
		return this.startAt.plusMinutes(attendanceMinute).isBefore(LocalDateTime.now());
	}

	public boolean isMatchCode(String code) {
		return this.code.equals(code);
	}
}
