package org.operation.web.alarm.dto.request;

import java.util.List;

import org.operation.alarm.domain.Alarm;
import org.operation.alarm.domain.Attribute;

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
