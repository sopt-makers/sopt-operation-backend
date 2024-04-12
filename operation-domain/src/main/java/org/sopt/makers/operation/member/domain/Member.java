package org.sopt.makers.operation.member.domain;

import java.util.ArrayList;
import java.util.List;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.common.domain.Part;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	protected Member(Long id, Long playgroundId, String name, int generation, ObYb obyb, Part part, String university, float score, String phone, List<Attendance> attendances) {
		this.id = id;
		this.playgroundId = playgroundId;
		this.name = name;
		this.generation = generation;
		this.obyb = obyb;
		this.part = part;
		this.university = university;
		this.score = score;
		this.phone = phone;
		this.attendances = attendances;
	}

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
