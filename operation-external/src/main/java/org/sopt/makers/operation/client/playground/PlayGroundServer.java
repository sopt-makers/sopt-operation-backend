package org.sopt.makers.operation.client.playground;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.web.alarm.dto.response.AlarmInactiveListResponse;

public interface PlayGroundServer {
	AlarmInactiveListResponse getInactiveMembers(int generation, Part part);
}
