package org.sopt.makers.operation.client.playground;

import org.sopt.makers.operation.client.playground.dto.MemberListGetResponse;
import org.sopt.makers.operation.member.domain.Part;

public interface PlayGroundServer {
	MemberListGetResponse getMembers(int generation, Part part);
}
