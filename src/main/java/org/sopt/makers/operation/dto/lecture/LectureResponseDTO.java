package org.sopt.makers.operation.dto.lecture;

import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;

public record LectureResponseDTO(
	Long lectureId,
	String name,
	int generation,
	Part part,
	Attribute attribute,
	List<SubLectureVO> subLectures,
	AttendanceInfo result

) {
	public static LectureResponseDTO of(Lecture lecture) {
		return new LectureResponseDTO(
			lecture.getId(), 
			lecture.getName(),
			lecture.getGeneration(),
			lecture.getPart(),
			lecture.getAttribute(),
			lecture.getSubLectures().stream().map(SubLectureVO::of).toList(),
			AttendanceInfo.of(lecture, lecture.getAttendances())
		);
	}
}

record SubLectureVO(
	Long subLectureId,
	int round,
	String startAt,
	String code
) {
	static SubLectureVO of(SubLecture subLecture) {
		String startAt = subLecture.getStartAt() != null ? subLecture.getStartAt().toString() : null;
		return new SubLectureVO(subLecture.getId(), subLecture.getRound(), startAt, subLecture.getCode());
	}
}

record AttendanceInfo(
	long attendance,
	long tardy,
	long absent,
	long unknown
) {
	static AttendanceInfo of(Lecture lecture, List<Attendance> attendances) {
		long count = attendances.stream().filter(info -> info.getStatus().equals(ABSENT)).count();
		long[] result = getCount(count, lecture.getEndDate());

		return new AttendanceInfo(
			attendances.stream().filter(info -> info.getStatus().equals(ATTENDANCE)).count(),
			attendances.stream().filter(info -> info.getStatus().equals(TARDY)).count(),
			result[0],
			result[1]
		);
	}

	private static long[] getCount(long count, LocalDateTime endDate) {
		return endDate.isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) ? new long[] {count, 0} : new long[] {0, count};
	}
}
