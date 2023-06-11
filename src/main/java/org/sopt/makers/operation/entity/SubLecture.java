package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;
import static org.sopt.makers.operation.entity.lecture.LectureStatus.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.entity.lecture.LectureStatus;

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

	@OneToMany(mappedBy = "subLecture", orphanRemoval = true)
	private final List<SubAttendance> subAttendances = new ArrayList<>();

	public SubLecture(Lecture lecture, int round) {
		setLecture(lecture);
		this.round = round;
	}

	public void startAttendance(String code) {
		this.startAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		this.code = code;
		this.lecture.updateStatus(getUpdatedStatus());
	}

	private LectureStatus getUpdatedStatus() {
		return switch (this.round) {
			case 1 -> FIRST;
			case 2 -> SECOND;
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
}
