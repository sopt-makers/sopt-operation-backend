package org.sopt.makers.operation.web.alarm.dto.response;

import static lombok.AccessLevel.PRIVATE;

import org.sopt.makers.operation.alarm.domain.Alarm;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AlarmCreateResponse(
		long id
) {

	public static AlarmCreateResponse of(Alarm alarm) {
		return AlarmCreateResponse.builder()
				.id(alarm.getId())
				.build();
	}
}
