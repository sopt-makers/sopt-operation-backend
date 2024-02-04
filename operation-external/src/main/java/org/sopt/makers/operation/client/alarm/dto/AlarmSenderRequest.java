package org.sopt.makers.operation.client.alarm.dto;

import java.util.List;

import org.sopt.makers.operation.domain.alarm.domain.Alarm;
import org.sopt.makers.operation.domain.alarm.domain.Attribute;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AlarmSenderRequest(
		String title,
		String content,
		List<String> targetList,
		Attribute attribute,
		String link
) {

	public static AlarmSenderRequest of (Alarm alarm, List<String> targetList) {
		return AlarmSenderRequest.builder()
				.title(alarm.getTitle())
				.content(alarm.getContent())
				.targetList(targetList)
				.attribute(alarm.getAttribute())
				.link(alarm.getLink())
				.build();
	}
}
