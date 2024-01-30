package org.sopt.makers.operation.external.playground;

import org.sopt.makers.operation.dto.alarm.response.AlarmInactiveListResponseDTO;
import org.sopt.makers.operation.entity.Part;

public interface PlayGroundServer {
	AlarmInactiveListResponseDTO getInactiveMembers(int generation, Part part);
}
