package org.sopt.makers.operation.domain.member.domain;

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

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.attendance.domain.Attendance;

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

	private String university;
	private float score;
	private String phone;

	@OneToMany(mappedBy = "member")
	List<Attendance> attendances = new ArrayList<>();

	public void updateScore(float score) {
		this.score += score;
	}

	public void updateTotalScore() {
		this.score = calcAllAttendances();
	}

	private float calcAllAttendances() {
		return (float) (2 + this.attendances.stream()
				.filter(Attendance::isEnd)
				.mapToDouble(Attendance::getScore)
				.sum());
	}
}
