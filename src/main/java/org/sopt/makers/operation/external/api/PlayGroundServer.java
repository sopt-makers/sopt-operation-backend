package org.sopt.makers.operation.external.api;

import org.sopt.makers.operation.dto.alarm.AlarmInactiveListResponseDTO;
import org.sopt.makers.operation.entity.Part;

public interface PlayGroundServer {
	AlarmInactiveListResponseDTO getInactiveMembers(int generation, Part part);
}
