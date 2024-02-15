package org.sopt.makers.operation.lecture.domain;

import static org.sopt.makers.operation.lecture.domain.LectureStatus.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sopt.makers.operation.common.domain.BaseEntity;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.attendance.domain.Attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Lecture extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lecture_id")
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private Part part;

	private int generation;

	private String place;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@Enumerated(EnumType.STRING)
	private Attribute attribute;

	@Enumerated(EnumType.STRING)
	private LectureStatus lectureStatus;

	@OneToMany(mappedBy = "lecture")
	List<SubLecture> subLectures = new ArrayList<>();

	@OneToMany(mappedBy = "lecture")
	List<Attendance> attendances = new ArrayList<>();

	@Builder
	public Lecture(
			String name,
			Part part,
			int generation,
			String place,
			LocalDateTime startDate,
			LocalDateTime endDate,
			Attribute attribute
	) {
		this.name = name;
		this.part = part;
		this.generation = generation;
		this.place = place;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attribute = attribute;
		this.lectureStatus = BEFORE;
	}

	public void updateStatus(LectureStatus status) {
		this.lectureStatus = status;
	}

	public void updateToEnd() {
		this.lectureStatus = END;
		attendances.forEach(Attendance::updateMemberScore);
	}

	public boolean isEnd() {
		return this.lectureStatus.equals(END);
	}

	public boolean isBefore() {
		return this.lectureStatus.equals(BEFORE);
	}

	public boolean isFirst() {
		return this.lectureStatus.equals(FIRST);
	}
}
