package org.sopt.makers.operation.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.sopt.makers.operation.dto.lecture.request.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.response.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.AttendancesStatusVO;
import org.sopt.makers.operation.dto.lecture.response.LectureDetailResponseDTO;
import org.sopt.makers.operation.dto.lecture.request.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.response.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.LectureResponseDTO.SubLectureVO;
import org.sopt.makers.operation.dto.lecture.response.LecturesResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.LecturesResponseDTO.LectureVO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.SubLecture;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.entity.lecture.LectureStatus;

public class LectureFixture {

	public static final String LECTURE_NAME = "테스트 이름";
	public static final Part LECTURE_PART = Part.ALL;
	public static final int LECTURE_GENERATION = 30;
	public static final String LECTURE_PLACE = "테스트 장소";
	public static final LocalDateTime NOW = LocalDateTime.now();
	public static final Attribute LECTURE_ATTRIBUTE = Attribute.ETC;
	public static final LectureStatus LECTURE_STATUS = LectureStatus.BEFORE;
	public static final int LIST_SIZE = 5;
	public static final String SUB_LECTURE_CODE = "code";

	public static LectureRequestDTO lectureRequest() {
		return LectureRequestDTO.builder()
				.part(LECTURE_PART)
				.name(LECTURE_NAME)
				.generation(LECTURE_GENERATION)
				.place(LECTURE_PLACE)
				.startDate(NOW.toString())
				.endDate(NOW.plusHours(4).toString())
				.attribute(LECTURE_ATTRIBUTE)
				.build();
	}

	public static Lecture lectureEnd() {
		Lecture lecture = lectureRequest().toEntity();
		lecture.updateToEnd();
		return lecture;
	}

	public static long lectureId() {
		return 0L;
	}

	public static LecturesResponseDTO lecturesResponse() {
		return new LecturesResponseDTO(LECTURE_GENERATION, lectures());
	}

	private static List<LectureVO> lectures() {
		return Stream.iterate(1, i -> i + 1).limit(LIST_SIZE)
				.map(LectureFixture::lecture).toList();
	}

	private static LectureVO lecture(int i) {
		return LectureVO.builder()
				.lectureId(0L)
				.name(LECTURE_NAME + i)
				.partValue(Part.ALL)
				.partName(Part.ALL.getName())
				.startDate(NOW.plusHours(i).toString())
				.endDate(NOW.plusHours(i + 4).toString())
				.attributeValue(LECTURE_ATTRIBUTE)
				.attributeName(LECTURE_ATTRIBUTE.getName())
				.place(LECTURE_PLACE + i)
				.attendances(attendancesStatus())
				.build();
	}

	public static LectureResponseDTO lectureResponse() {
		return LectureResponseDTO.builder()
				.lectureId(0L)
				.name(LECTURE_NAME)
				.generation(LECTURE_GENERATION)
				.part(LECTURE_PART)
				.attribute(LECTURE_ATTRIBUTE)
				.subLectures(subLectures())
				.attendances(attendancesStatus())
				.status(LECTURE_STATUS)
				.build();
	}

	private static AttendancesStatusVO attendancesStatus() {
		return AttendancesStatusVO.builder()
				.attendance(80)
				.absent(0)
				.tardy(10)
				.unknown(10)
				.build();
	}

	private static List<SubLectureVO> subLectures() {
		return Stream.iterate(1, i -> i + 1).limit(LIST_SIZE)
				.map(LectureFixture::subLecture).toList();
	}

	private static SubLectureVO subLecture(int i) {
		return SubLectureVO.builder()
				.subLectureId((long)i)
				.round(i % 2)
				.startAt(NOW.toString())
				.code(SUB_LECTURE_CODE)
				.build();
	}

	public static AttendanceResponseDTO attendanceResponse() {
		return new AttendanceResponseDTO(0L, 0L);
	}

	public static AttendanceRequestDTO attendanceRequest() {
		return new AttendanceRequestDTO(0L, 1, SUB_LECTURE_CODE);
	}

	public static LectureDetailResponseDTO lectureDetail() {
		return LectureDetailResponseDTO.builder()
				.lectureId(0L)
				.part(LECTURE_PART.getName())
				.name(LECTURE_NAME)
				.place(LECTURE_PLACE)
				.attribute(LECTURE_ATTRIBUTE.getName())
				.startDate(NOW.toString())
				.endDate(NOW.plusHours(4).toString())
				.generation(LECTURE_GENERATION)
				.build();
	}

	public static List<Lecture> lectureList() {
		return Stream.iterate(1, i -> i + 1).limit(LIST_SIZE)
				.map(i -> lectureRequest().toEntity()).toList();
	}

	public static SubLecture subLecture(Lecture lecture) {
		return new SubLecture(lecture, 1);
	}
}
