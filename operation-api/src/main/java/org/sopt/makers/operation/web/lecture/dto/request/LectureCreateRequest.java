package org.sopt.makers.operation.web.lecture.dto.request;

import static lombok.AccessLevel.*;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.exception.DateTimeParseCustomException;

import lombok.Builder;
import lombok.NonNull;

@Builder(access = PRIVATE)
public record LectureCreateRequest(
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

	private LocalDateTime convertLocalDateTime(String date) {
		try {
			return LocalDateTime.parse(date);
		} catch (DateTimeParseException exception) {
			throw new DateTimeParseCustomException(INVALID_DATE_PATTERN, date, exception.getErrorIndex());
		}
	}
}
