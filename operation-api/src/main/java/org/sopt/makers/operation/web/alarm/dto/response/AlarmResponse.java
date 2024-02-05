package org.sopt.makers.operation.web.alarm.dto.response;

import static java.util.Objects.*;

import java.time.LocalDateTime;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.alarm.domain.Alarm;

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
			.part(getPartName(alarm.getPart()))
			.isActive(alarm.getIsActive())
			.title(alarm.getTitle())
			.content(alarm.getContent())
			.link(alarm.getLink())
			.createdAt(alarm.getCreatedDate().toString())
			.sendAt(getSendAt(alarm.getSendAt()))
			.build();
	}

	private static String getPartName(Part part) {
		return nonNull(part) ? part.getName() : null;
	}

	private static String getSendAt(LocalDateTime sendAt) {
		return nonNull(sendAt) ? sendAt.toString() : null;
	}
}
