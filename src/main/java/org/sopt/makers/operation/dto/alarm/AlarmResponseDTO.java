package org.sopt.makers.operation.dto.alarm;

import static java.util.Objects.*;

import org.sopt.makers.operation.entity.alarm.Alarm;

import lombok.Builder;

@Builder
public record AlarmResponseDTO(
	String attribute,
	String part,
	Boolean isActive,
	String title,
	String content,
	String link,
	String createdAt,
	String sendAt
) {
	public static AlarmResponseDTO of(Alarm alarm) {
		return AlarmResponseDTO.builder()
			.attribute(alarm.getAttribute().getName())
			.part(alarm.getPart().getName())
			.isActive(alarm.getIsActive())
			.title(alarm.getTitle())
			.content(alarm.getContent())
			.link(alarm.getLink())
			.createdAt(alarm.getCreatedDate().toString())
			.sendAt(nonNull(alarm.getSendAt()) ? alarm.getSendAt().toString() : null)
			.build();
	}
}
