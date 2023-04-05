package org.sopt.makers.operation.entity;

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

import org.sopt.makers.operation.entity.lecture.Lecture;

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

	@OneToMany(mappedBy = "subLecture")
	private final List<SubAttendance> subAttendances = new ArrayList<>();

	public SubLecture(Lecture lecture, int round) {
		setLecture(lecture);
		this.round = round;
	}

	public void startAttendance() {
		this.startAt = LocalDateTime.now();
	}

	private void setLecture(Lecture lecture) {
		if (Objects.nonNull(this.lecture)) {
			this.lecture.getSubLectures().remove(this);
		}
		this.lecture = lecture;
		lecture.getSubLectures().add(this);
	}
}
