package org.sopt.makers.operation.dto.lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.entity.Part;

import lombok.NonNull;

public record LectureRequestDTO(
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		return LocalDateTime.parse(date, formatter);
	}
}
