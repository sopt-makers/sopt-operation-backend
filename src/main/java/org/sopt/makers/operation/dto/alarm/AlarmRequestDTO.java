package org.sopt.makers.operation.dto.alarm;

import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Alarm;
import org.sopt.makers.operation.entity.alarm.Attribute;
import org.sopt.makers.operation.entity.alarm.Status;

public record AlarmRequestDTO(
	int generation,
	Attribute attribute,
	String title,
	String content,
	String link,
	Boolean isActive,
	Part part,
	List<Long> targetList,
	Status status
) {
	public Alarm toEntity() {
		return new Alarm(this);
	}
}
