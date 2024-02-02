package org.operation.web.lecture.dto.response;

import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.util.List;

import org.operation.common.domain.Part;
import org.operation.lecture.Attribute;
import org.operation.lecture.Lecture;
import org.operation.lecture.LectureStatus;
import org.operation.lecture.SubLecture;

import lombok.Builder;

@Builder
public record LectureResponse(
	Long lectureId,
	String name,
	int generation,
	Part part,
	Attribute attribute,
	List<SubLectureVO> subLectures,
	AttendancesStatusVO attendances,
	LectureStatus status

) {
	public static LectureResponse of(Lecture lecture) {
		return LectureResponse.builder()
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