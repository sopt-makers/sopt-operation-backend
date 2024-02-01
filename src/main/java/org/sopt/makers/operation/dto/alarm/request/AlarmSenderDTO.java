package org.sopt.makers.operation.dto.alarm.request;

import java.util.List;

import org.operation.alarm.Alarm;
import org.operation.alarm.Attribute;

import lombok.Builder;

@Builder
public record AlarmSenderDTO(
	String title,
	String content,
	List<String> targetList,
	Attribute attribute,
	String link
) {
	public static AlarmSenderDTO of (Alarm alarm, List<String> targetList) {
		return AlarmSenderDTO.builder()
			.title(alarm.getTitle())
			.content(alarm.getContent())
			.targetList(targetList)
			.attribute(alarm.getAttribute())
			.link(alarm.getLink())
			.build();
	}
}
