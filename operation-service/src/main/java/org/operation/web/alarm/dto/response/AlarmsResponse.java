package org.operation.web.alarm.dto.response;

import static java.util.Objects.*;

import java.util.List;

import org.operation.alarm.domain.Alarm;

import lombok.Builder;

public record AlarmsResponse(
	List<AlarmVO> alarms,
	int totalCount
) {
	public static AlarmsResponse of(List<Alarm> alarms, int totalCount) {
		return new AlarmsResponse(alarms.stream().map(AlarmVO::of).toList(), totalCount);
	}

	@Builder
	record AlarmVO(
		Long alarmId,
		String part,
		String attribute,
		String title,
		String content,
		String sendAt,
		String status
	) {
		static AlarmVO of(Alarm alarm) {
			return AlarmVO.builder()
				.alarmId(alarm.getId())
				.part(nonNull(alarm.getPart()) ? alarm.getPart().getName() : null)
				.attribute(alarm.getAttribute().getName())
				.title(alarm.getTitle())
				.content(alarm.getContent())
				.sendAt(nonNull(alarm.getSendAt()) ? alarm.getSendAt().toString() : null)
				.status(alarm.getStatus().getName())
				.build();
		}
	}
}
