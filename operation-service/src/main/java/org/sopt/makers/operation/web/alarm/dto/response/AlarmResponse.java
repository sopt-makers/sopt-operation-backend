package org.sopt.makers.operation.web.alarm.dto.response;

import static java.util.Objects.*;

import org.operation.alarm.domain.Alarm;

import lombok.Builder;

@Builder
public record AlarmResponse(
	String attribute,
	String part,
	Boolean isActive,
	String title,
	String content,
	String link,
	String createdAt,
	String sendAt
) {
	public static AlarmResponse of(Alarm alarm) {
		return AlarmResponse.builder()
			.attribute(alarm.getAttribute().getName())
			.part(nonNull(alarm.getPart()) ? alarm.getPart().getName() : null)
			.isActive(alarm.getIsActive())
			.title(alarm.getTitle())
			.content(alarm.getContent())
			.link(alarm.getLink())
			.createdAt(alarm.getCreatedDate().toString())
			.sendAt(nonNull(alarm.getSendAt()) ? alarm.getSendAt().toString() : null)
			.build();
	}
}
