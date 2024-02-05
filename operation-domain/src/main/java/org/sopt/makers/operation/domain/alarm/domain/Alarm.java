package org.sopt.makers.operation.domain.alarm.domain;

import static java.util.Objects.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static org.sopt.makers.operation.domain.alarm.domain.Status.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.sopt.makers.operation.domain.BaseEntity;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.converter.StringListConverter;

import lombok.Builder;
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

	@Builder
	public Alarm(
			int generation,
			int generationAt,
			Attribute attribute,
			String title,
			String content,
			String link,
			Boolean isActive,
			Part part,
			List<String> targetList
	) {
		this.generation = generation;
		this.generationAt = generationAt;
		this.attribute = attribute;
		this.title = title;
		this.content = content;
		setLink(link);
		setTargetsInfo(isActive, part, targetList);
		this.status = BEFORE;
	}

	private void setLink(String link) {
		if (nonNull(link)) {
			this.link = link;
		}
	}

	private void setTargetsInfo(Boolean isActive, Part part, List<String> targetList) {
		if (nonNull(targetList)) {
			this.targetList = targetList;
		} else {
			this.isActive = isActive;
			this.part = part;
			this.targetList = new ArrayList<>();
		}
	}

	public boolean isSent() {
		return this.status.equals(AFTER);
	}

	public boolean hasEmptyTargetList() {
		return this.targetList.isEmpty();
	}

	public void updateToSent() {
		this.status = AFTER;
		this.sendAt = LocalDateTime.now();
	}
}
