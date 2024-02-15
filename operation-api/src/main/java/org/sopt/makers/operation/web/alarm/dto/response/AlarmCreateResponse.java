package org.sopt.makers.operation.web.alarm.dto.response;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.alarm.domain.Alarm;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AlarmCreateResponse(
		long alarmId
) {

	public static AlarmCreateResponse of(Alarm alarm) {
		return AlarmCreateResponse.builder()
				.alarmId(alarm.getId())
				.build();
	}
}
