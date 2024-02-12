package org.sopt.makers.operation.client.playground.dto;

import java.util.List;

public record InactiveMemberListResponse(
		List<Long> memberIds
) {
}
