package org.sopt.makers.operation.client.playground;

import org.sopt.makers.operation.client.playground.dto.InactiveMemberListResponse;
import org.sopt.makers.operation.domain.Part;

public interface PlayGroundServer {
	InactiveMemberListResponse getInactiveMembers(int generation, Part part);
}
