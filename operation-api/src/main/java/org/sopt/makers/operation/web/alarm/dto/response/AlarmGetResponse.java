package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Alarm;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AlarmGetResponse(
	String attribute,
	@JsonInclude(value = NON_NULL)
	String part,
	Boolean isActive,
	String title,
	String content,
	String link,
	String createdAt,
	@JsonInclude(value = NON_NULL)
	String sendAt
) {

	public static AlarmGetResponse of(Alarm alarm) {
		return AlarmGetResponse.builder()
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
