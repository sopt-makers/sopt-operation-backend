package org.sopt.makers.operation.dto;

import org.sopt.makers.operation.entity.Part;

import lombok.NonNull;

public record LectureRequestDTO(
	@NonNull Part part,
	@NonNull String name,
	String place,
	String startDate,
	String endDate,
	@NonNull String attribute
) {
}
