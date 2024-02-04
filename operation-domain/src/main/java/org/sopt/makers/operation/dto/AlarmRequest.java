package org.sopt.makers.operation.dto;

import java.util.List;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.alarm.domain.Alarm;
import org.sopt.makers.operation.domain.alarm.domain.Attribute;

public record AlarmRequest(
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
	public Alarm toEntity() {
		return new Alarm(this);
	}
}
