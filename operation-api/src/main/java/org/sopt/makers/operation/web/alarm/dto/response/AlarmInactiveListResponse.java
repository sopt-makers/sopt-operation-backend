package org.sopt.makers.operation.web.alarm.dto.response;

import java.util.List;

public record AlarmInactiveListResponse(
		List<Long> memberIds
) {
}
