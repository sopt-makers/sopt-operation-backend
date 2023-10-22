package org.sopt.makers.operation.entity.alarm;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.sopt.makers.operation.converter.LongListConverter;
import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.entity.BaseEntity;
import org.sopt.makers.operation.entity.Part;

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

	@Column(nullable = false)
	@Enumerated(value = STRING)
	private Attribute attribute;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	private String link;

	private boolean isActive;

	@Enumerated(value = STRING)
	private Part part;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = LongListConverter.class)
	private List<Long> targetList;

	@Column(nullable = false)
	@Enumerated(value = STRING)
	private Status status;

	private LocalDateTime sendAt;

	public Alarm(AlarmRequestDTO requestDTO) {
		this.generation = requestDTO.generation();
		this.attribute = requestDTO.attribute();
		this.title = requestDTO.title();
		this.content = requestDTO.content();
		this.link = requestDTO.link();
		this.isActive = requestDTO.isActive();
		this.part = requestDTO.part();
		this.targetList = requestDTO.targetList();
	}
}
