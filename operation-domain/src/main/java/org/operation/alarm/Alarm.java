package org.operation.alarm;

import static java.util.Objects.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.sopt.makers.operation.converter.StringListConverter;
import org.sopt.makers.operation.dto.alarm.request.AlarmRequestDTO;
import org.operation.BaseEntity;
import org.operation.Part;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Alarm extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "alarm_id")
	private Long id;

	private int generation;

	private int generationAt;

	@Column(nullable = false)
	@Enumerated(value = STRING)
	private Attribute attribute;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	private String link;

	private Boolean isActive;

	@Enumerated(value = STRING)
	private Part part;

	@Column(columnDefinition = "TEXT", nullable = false)
	@Convert(converter = StringListConverter.class)
	private List<String> targetList;

	@Column(nullable = false)
	@Enumerated(value = STRING)
	private Status status;

	private LocalDateTime sendAt;

	public void updateStatus() {
		this.status = Status.AFTER;
	}

	public void updateSendAt() {
		this.sendAt = LocalDateTime.now();
	}

	public Alarm(AlarmRequestDTO requestDTO) {
		this.generation = requestDTO.generation();
		this.generationAt = requestDTO.generationAt();
		this.attribute = requestDTO.attribute();
		this.title = requestDTO.title();
		this.content = requestDTO.content();
		if (nonNull(requestDTO.link())) {
			this.link = requestDTO.link();
		}
		if (nonNull(requestDTO.isActive())) {
			this.isActive = requestDTO.isActive();
		}
		if (nonNull(requestDTO.part())) {
			this.part = requestDTO.part();
		}
		this.targetList = nonNull(requestDTO.targetList()) ? requestDTO.targetList() : new ArrayList<>();
		this.status = Status.BEFORE;
	}
}
