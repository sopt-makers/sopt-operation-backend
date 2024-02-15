package org.sopt.makers.operation.web.lecture.dto.response;

import static java.util.Objects.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.LectureStatus;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record LectureGetResponse(
	long lectureId,
	String name,
	int generation,
	Part part,
	Attribute attribute,
	List<SubLectureResponse> subLectures,
	AttendanceStatusListResponse attendances,
	LectureStatus status

) {

	public static LectureGetResponse of(Lecture lecture) {
		return LectureGetResponse.builder()
			.lectureId(lecture.getId())
			.name(lecture.getName())
			.generation(lecture.getGeneration())
			.part(lecture.getPart())
			.attribute(lecture.getAttribute())
			.subLectures(lecture.getSubLectures().stream().map(SubLectureResponse::of).toList())
			.attendances(AttendanceStatusListResponse.of(lecture))
			.status(lecture.getLectureStatus())
			.build();
	}

	@Builder(access = PRIVATE)
	public record SubLectureResponse(
			long subLectureId,
			int round,
			String startAt,
			String code
	) {

		private static SubLectureResponse of(SubLecture subLecture) {
			return SubLectureResponse.builder()
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