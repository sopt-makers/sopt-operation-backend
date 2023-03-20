package org.sopt.makers.operation.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Session {

	@Id @GeneratedValue
	@Column(name = "session_id")
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private Part part;

	@OneToMany(mappedBy = "session")
	List<SubSession> subSessions = new ArrayList<>();

	@OneToMany(mappedBy = "session")
	List<Attendance> attendances = new ArrayList<>();

	public Session(String name, Part part) {
		this.name = name;
		this.part = part;
	}
}
