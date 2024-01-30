package org.sopt.makers.operation.entity.member;

import static org.sopt.makers.operation.entity.lecture.LectureStatus.*;

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

import org.sopt.makers.operation.dto.member.request.MemberRequestDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.attendance.Attendance;

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

	public Member(MemberRequestDTO requestDTO) {
		this.playgroundId = requestDTO.playgroundId();
		this.name = requestDTO.name();
		this.generation = requestDTO.generation();
		this.obyb = requestDTO.obyb();
		this.part = requestDTO.part();
		this.university = requestDTO.university();
		this.phone = requestDTO.phone();
		this.score = 2;
	}

	public void updateScore(float score) {
		this.score += score;
	}

	public void updateTotalScore() {
		this.score = (float) (2 + this.attendances.stream()
			.filter(attendance -> attendance.getLecture().getLectureStatus().equals(END))
			.mapToDouble(Attendance::getScore)
			.sum());
	}
}
