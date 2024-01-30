package org.sopt.makers.operation.dto.lecture.response;

import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.SubLecture;
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

	@Builder
	public record SubLectureVO(
			Long subLectureId,
			int round,
			String startAt,
			String code
	) {
		private static SubLectureVO of(SubLecture subLecture) {
			return SubLectureVO.builder()
					.subLectureId(subLecture.getId())
					.round(subLecture.getRound())
					.startAt(getStartAt(subLecture.getStartAt()))
					.code(subLecture.getCode())
					.build();
		}

		private static String getStartAt(LocalDateTime startAt) {
			return nonNull(startAt) ? startAt.toString() : null;
		}
	}
}