package org.sopt.makers.operation.dto.alarm;

import static java.util.Objects.*;

import java.util.List;

import org.sopt.makers.operation.entity.alarm.Alarm;

import lombok.Builder;

public record AlarmsResponseDTO(
	List<AlarmVO> alarms
) {
	public static AlarmsResponseDTO of(List<Alarm> alarms) {
		return new AlarmsResponseDTO(alarms.stream().map(AlarmVO::of).toList());
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
				.part(alarm.getPart().getName())
				.attribute(alarm.getAttribute().getName())
				.title(alarm.getTitle())
				.content(alarm.getContent())
				.sendAt(nonNull(alarm.getSendAt()) ? alarm.getSendAt().toString() : null)
				.status(alarm.getStatus().getName())
				.build();
		}
	}
}
