package org.operation.web.alarm.dto.request;

import java.util.List;

import org.operation.common.domain.Part;
import org.operation.alarm.domain.Alarm;
import org.operation.alarm.domain.Attribute;

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
