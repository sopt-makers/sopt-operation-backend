package org.sopt.makers.operation.web.alarm.dto.request;

import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.Attribute;

public record AlarmCreateRequest(
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
		return Alarm.builder()
				.generation(this.generation)
				.generationAt(this.generationAt)
				.attribute(this.attribute)
				.title(this.title)
				.content(this.content)
				.link(this.link)
				.isActive(this.isActive)
				.part(this.part)
				.targetList(this.targetList)
				.build();
	}
}
