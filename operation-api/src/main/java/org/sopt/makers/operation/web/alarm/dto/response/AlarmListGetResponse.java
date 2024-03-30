package org.sopt.makers.operation.web.alarm.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Alarm;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AlarmListGetResponse(
	List<AlarmResponse> alarms,
	int totalCount
) {

	public static AlarmListGetResponse of(List<Alarm> alarmList, int totalCount) {
		return AlarmListGetResponse.builder()
				.alarms(alarmList.stream().map(AlarmResponse::of).toList())
				.totalCount(totalCount)
				.build();
	}

	@Builder(access = PRIVATE)
	private record AlarmResponse(
		long alarmId,
		@JsonInclude(value = NON_NULL)
		String part,
		String attribute,
		String title,
		String content,
		@JsonInclude(value = NON_NULL)
		String sendAt,
		String status
	) {

		private static AlarmResponse of(Alarm alarm) {
			return AlarmResponse.builder()
				.alarmId(alarm.getId())
				.part(getPartName(alarm.getPart()))
				.attribute(alarm.getAttribute().getName())
				.title(alarm.getTitle())
				.content(alarm.getContent())
				.sendAt(getSendAt(alarm.getSendAt()))
				.status(alarm.getStatus().getName())
				.build();
		}

		private static String getPartName(Part part) {
			return nonNull(part) ? part.getName() : null;
		}

		private static String getSendAt(LocalDateTime sendAt) {
			return nonNull(sendAt) ? sendAt.toString() : null;
		}
	}
}
