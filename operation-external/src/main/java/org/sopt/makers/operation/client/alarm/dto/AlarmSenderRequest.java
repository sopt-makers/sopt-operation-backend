package org.sopt.makers.operation.client.alarm.dto;

import static org.sopt.makers.operation.domain.alarm.domain.Attribute.*;

import java.util.List;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.domain.alarm.domain.Alarm;
import org.sopt.makers.operation.domain.alarm.domain.Attribute;
import org.sopt.makers.operation.domain.lecture.Lecture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.val;

@Builder(access = AccessLevel.PRIVATE)
public record AlarmSenderRequest(
		String title,
		String content,
		List<String> targetList,
		Attribute attribute,
		String link
) {

	public static AlarmSenderRequest of(Alarm alarm, List<String> targetList) {
		return AlarmSenderRequest.builder()
				.title(alarm.getTitle())
				.content(alarm.getContent())
				.targetList(targetList)
				.attribute(alarm.getAttribute())
				.link(alarm.getLink())
				.build();
	}

	public static AlarmSenderRequest of(Lecture lecture, ValueConfig valueConfig) {
		val title = lecture.getName() + " " + valueConfig.getALARM_MESSAGE_TITLE();
		val content = valueConfig.getALARM_MESSAGE_CONTENT();
		val targetList = getTargetsFromLecture(lecture);
		return AlarmSenderRequest.builder()
				.title(title)
				.content(content)
				.targetList(targetList)
				.attribute(NEWS)
				.build();
	}

	private static List<String> getTargetsFromLecture(Lecture lecture) {
		return lecture.getAttendances().stream()
				.map(attendance -> String.valueOf(attendance.getMember().getPlaygroundId()))
				.filter(id -> !id.equals("null"))
				.toList();
	}

}
