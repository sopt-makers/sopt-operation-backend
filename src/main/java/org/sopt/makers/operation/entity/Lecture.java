package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
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
public class Lecture extends BaseEntity {

	@Id @GeneratedValue(strategy = IDENTITY)
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

	@OneToMany(mappedBy = "lecture")
	List<SubLecture> subLectures = new ArrayList<>();

	@OneToMany(mappedBy = "lecture")
	List<Attendance> attendances = new ArrayList<>();
}
