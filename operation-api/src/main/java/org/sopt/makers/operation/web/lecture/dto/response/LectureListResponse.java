package org.sopt.makers.operation.web.lecture.dto.response;

import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record LectureListResponse(
		int generation,
		List<LectureVO> lectures
) {

	public static LectureListResponse of(int generation, List<Lecture> lectureList) {
		return LectureListResponse.builder()
				.generation(generation)
				.lectures(lectureList.stream().map(LectureVO::of).toList())
				.build();
	}

	@Builder
	public record LectureVO(
			Long lectureId,
			String name,
			Part partValue,
			String partName,
			String startDate,
			String endDate,
			Attribute attributeValue,
			String attributeName,
			String place,
			AttendanceStatusListVO attendances
	) {
		private static LectureVO of(Lecture lecture) {
			return LectureVO.builder()
					.lectureId(lecture.getId())
					.name(lecture.getName())
					.partValue(lecture.getPart())
					.partName(lecture.getPart().getName())
					.startDate(lecture.getStartDate().toString())
					.endDate(lecture.getEndDate().toString())
					.attributeValue(lecture.getAttribute())
					.attributeName(lecture.getAttribute().getName())
					.place(lecture.getPlace())
					.attendances(AttendanceStatusListVO.of(lecture))
					.build();
		}
	}
}
