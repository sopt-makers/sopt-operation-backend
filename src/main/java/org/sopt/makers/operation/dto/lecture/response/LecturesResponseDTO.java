package org.sopt.makers.operation.dto.lecture.response;

import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.operation.lecture.Attribute;
import org.operation.lecture.Lecture;

import lombok.*;

public record LecturesResponseDTO(
		int generation,
		List<LectureVO> lectures
) {

	public static LecturesResponseDTO of(int generation, List<Lecture> lectures) {
		return new LecturesResponseDTO(
				generation,
				lectures.stream().map(LectureVO::of).toList()
		);
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
			AttendancesStatusVO attendances
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
					.attendances(AttendancesStatusVO.of(lecture))
					.build();
		}
	}
}

