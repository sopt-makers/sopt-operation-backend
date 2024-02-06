package org.sopt.makers.operation.web.alarm.dto.response;

import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.alarm.domain.Alarm;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AlarmListResponse(
	List<AlarmVO> alarms,
	int totalCount
) {
	public static AlarmListResponse of(List<Alarm> alarmList, int totalCount) {
		return AlarmListResponse.builder()
				.alarms(alarmList.stream().map(AlarmVO::of).toList())
				.totalCount(totalCount)
				.build();
	}

	@Builder
	private record AlarmVO(
		long alarmId,
		String part,
		String attribute,
		String title,
		String content,
		String sendAt,
		String status
	) {
		private static AlarmVO of(Alarm alarm) {
			return AlarmVO.builder()
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
