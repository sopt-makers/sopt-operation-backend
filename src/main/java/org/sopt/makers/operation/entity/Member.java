package org.sopt.makers.operation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private Long playgroundId;
	private String name;
	private int generation;
	private ObYb obyb;

	@Enumerated(EnumType.STRING)
	private Part part;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String university;
	private int score;
	private String phone;

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
}
