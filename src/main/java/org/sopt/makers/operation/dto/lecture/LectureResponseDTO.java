package org.sopt.makers.operation.dto.lecture;

import static java.util.Objects.*;
import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.entity.lecture.LectureStatus;

import lombok.*;

@Builder
public record LectureResponseDTO(
	Long lectureId,
	String name,
	int generation,
	Part part,
	Attribute attribute,
	List<SubLectureVO> subLectures,
	AttendancesStatusVO attendances,
	LectureStatus status

) {
	public static LectureResponseDTO of(Lecture lecture) {
		return LectureResponseDTO.builder()
			.lectureId(lecture.getId())
			.name(lecture.getName())
			.generation(lecture.getGeneration())
			.part(lecture.getPart())
			.attribute(lecture.getAttribute())
			.subLectures(lecture.getSubLectures().stream().map(SubLectureVO::of).toList())
			.attendances(AttendancesStatusVO.of(lecture))
			.status(lecture.getLectureStatus())
			.build();
	}
}

record SubLectureVO(
	Long subLectureId,
	int round,
	String startAt,
	String code
) {
	static SubLectureVO of(SubLecture subLecture) {
		val startAt = nonNull(subLecture.getStartAt()) ? subLecture.getStartAt().toString() : null;
		return new SubLectureVO(subLecture.getId(), subLecture.getRound(), startAt, subLecture.getCode());
	}
}