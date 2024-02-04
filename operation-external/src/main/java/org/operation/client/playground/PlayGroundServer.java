package org.operation.client.playground;

import org.operation.common.domain.Part;
import org.operation.web.alarm.dto.response.AlarmInactiveListResponse;

public interface PlayGroundServer {
	AlarmInactiveListResponse getInactiveMembers(int generation, Part part);
}
