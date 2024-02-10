package org.sopt.makers.operation.alarm.domain;

import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sopt.makers.operation.common.domain.BaseEntity;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.schedule.converter.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Alarm extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alarm_id")
	private Long id;

	private int generation;

	private int generationAt;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Attribute attribute;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	private String link;

	private Boolean isActive;

	@Enumerated(value = EnumType.STRING)
	private Part part;

	@Column(columnDefinition = "TEXT", nullable = false)
	@Convert(converter = StringListConverter.class)
	private List<String> targetList;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
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
		this.status = Status.BEFORE;
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
		return this.status.equals(Status.AFTER);
	}

	public boolean hasEmptyTargetList() {
		return this.targetList.isEmpty();
	}

	public void updateToSent() {
		this.status = Status.AFTER;
		this.sendAt = LocalDateTime.now();
	}
}
