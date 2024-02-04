package org.sopt.makers.operation.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record AlarmSenderRequest(
	String title,
	String content,
	List<String> targetList,
	Attribute attribute,
	String link
) {
	public static AlarmSenderRequest of (Alarm alarm, List<String> targetList) {
		return builder()
			.title(alarm.getTitle())
			.content(alarm.getContent())
			.targetList(targetList)
			.attribute(alarm.getAttribute())
			.link(alarm.getLink())
			.build();
	}
}
