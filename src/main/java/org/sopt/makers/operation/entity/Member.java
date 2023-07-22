package org.sopt.makers.operation.entity;

import static org.sopt.makers.operation.entity.lecture.LectureStatus.*;
import static org.sopt.makers.operation.util.Generation32.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.sopt.makers.operation.entity.lecture.Lecture;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private Long playgroundId;
	private String name;
	private int generation;

	@Enumerated(EnumType.STRING)
	private ObYb obyb;

	@Enumerated(EnumType.STRING)
	private Part part;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String university;
	private float score;
	private String phone;

	@OneToMany(mappedBy = "member")
	List<Attendance> attendances = new ArrayList<>();

	public Member(Long playgroundId, String name, int generation, ObYb obyb, Part part,
		Gender gender, String university, String phone) {

		this.playgroundId = playgroundId;
		this.name = name;
		this.generation = generation;
		this.obyb = obyb;
		this.part = part;
		this.gender = gender;
		this.university = university;
		this.phone = phone;
		this.score = 2;
	}

	public void updateScore(float score) {
		this.score += score;
	}

	public void updateTotalScore() {
		this.score = (float) (2 + this.attendances.stream()
			.mapToDouble(attendance -> {
				Lecture lecture = attendance.getLecture();
				return lecture.getLectureStatus().equals(END)
					? getUpdateScore(lecture.getAttribute(), attendance.getStatus())
					: 0;
			})
			.sum());
	}
}
