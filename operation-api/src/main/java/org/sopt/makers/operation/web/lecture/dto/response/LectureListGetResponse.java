package org.sopt.makers.operation.web.lecture.dto.response;

import static lombok.AccessLevel.*;

import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record LectureListGetResponse(
		int generation,
		List<LectureResponse> lectures
) {

	public static LectureListGetResponse of(int generation, List<Lecture> lectureList) {
		return LectureListGetResponse.builder()
				.generation(generation)
				.lectures(lectureList.stream().map(LectureResponse::of).toList())
				.build();
	}

	@Builder(access = PRIVATE)
	public record LectureResponse(
			long lectureId,
			String name,
			Part partValue,
			String partName,
			String startDate,
			String endDate,
			Attribute attributeValue,
			String attributeName,
			String place,
			AttendanceStatusListResponse attendances
	) {

		private static LectureResponse of(Lecture lecture) {
			return LectureResponse.builder()
					.lectureId(lecture.getId())
					.name(lecture.getName())
					.partValue(lecture.getPart())
					.partName(lecture.getPart().getName())
					.startDate(lecture.getStartDate().toString())
					.endDate(lecture.getEndDate().toString())
					.attributeValue(lecture.getAttribute())
					.attributeName(lecture.getAttribute().getName())
					.place(lecture.getPlace())
					.attendances(AttendanceStatusListResponse.of(lecture))
					.build();
		}
	}
}
