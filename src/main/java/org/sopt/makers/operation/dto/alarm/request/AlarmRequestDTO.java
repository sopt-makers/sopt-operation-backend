package org.sopt.makers.operation.dto.alarm.request;

import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.operation.alarm.Alarm;
import org.operation.alarm.Attribute;

public record AlarmRequestDTO(
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
