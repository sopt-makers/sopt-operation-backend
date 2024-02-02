package org.operation.web.lecture.dto.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.operation.common.domain.Part;
import org.operation.lecture.Attribute;
import org.operation.lecture.Lecture;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record LectureRequest(
	@NonNull Part part,
	@NonNull String name,
	int generation,
	String place,
	String startDate,
	String endDate,
	@NonNull Attribute attribute
) {

	public Lecture toEntity() {
		return Lecture.builder()
			.name(this.name)
			.part(this.part)
			.generation(this.generation)
			.place(this.place)
			.startDate(convertLocalDateTime(this.startDate))
			.endDate(convertLocalDateTime(this.endDate))
			.attribute(this.attribute)
			.build();
	}

	private LocalDateTime convertLocalDateTime(String date) throws DateTimeParseException {
		return LocalDateTime.parse(date);
	}
}
