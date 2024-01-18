package org.sopt.makers.operation.controller.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.AttendancesStatusVO;
import org.sopt.makers.operation.dto.lecture.LectureDetailResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO.SubLectureVO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO.LectureVO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.LectureStatus;
import org.sopt.makers.operation.service.LectureService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {
	@InjectMocks
	private LectureController lectureController;

	@Mock
	private LectureService lectureService;

	private MockMvc mockMvc;

	private final String DEFAULT_URI = "/api/v1/lectures";
	private final String LECTURE_NAME = "테스트 이름";
	private final Part LECTURE_PART = Part.ALL;
	private final int LECTURE_GENERATION = 30;
	private final String LECTURE_PLACE = "테스트 장소";
	private final LocalDateTime NOW = LocalDateTime.now();
	private final Attribute LECTURE_ATTRIBUTE = Attribute.ETC;
	private final LectureStatus LECTURE_STATUS = LectureStatus.BEFORE;
	private final int LIST_SIZE = 5;
	private final String SUB_LECTURE_CODE = "code";

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
	}

	@DisplayName("세션 생성 성공")
	@Test
	void success_createLecture() throws Exception {
		// given
		LectureRequestDTO request = lectureRequest();
		long response = lectureId();

		doReturn(response).when(lectureService).createLecture(any());

		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post(DEFAULT_URI)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(request)));

		// then
		resultActions.andExpect(status().isCreated());
	}

	@DisplayName("세션 목록 조회 성공")
	@Test
	void success_getLectureList() throws Exception {
		// given
		LecturesResponseDTO response = lecturesResponse();
		MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
		queries.add("generation", String.valueOf(LECTURE_GENERATION));

		doReturn(response).when(lectureService).getLecturesByGeneration(anyInt(), any());

		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.get(DEFAULT_URI)
						.queryParams(queries));

		// then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("세션 단일 조회 성공")
	@Test
	void success_getLecture() throws Exception {
		// given
		LectureResponseDTO response = lectureResponse();

		doReturn(response).when(lectureService).getLecture(anyLong());

		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.get(DEFAULT_URI + "/{lectureId}", anyLong()));

		// then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("출석 시작 성공")
	@Test
	void success_startAttendance() throws Exception {
		// given
		AttendanceRequestDTO request = attendanceRequest();
		AttendanceResponseDTO response = attendanceResponse();

		doReturn(response).when(lectureService).startAttendance(any());

		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.patch(DEFAULT_URI + "/attendance")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(request)));

		// then
		resultActions.andExpect(status().isCreated());
	}

	@DisplayName("출석 종료 성공")
	@Test
	void success_finishLecture() throws Exception {
		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.patch(DEFAULT_URI + "/{lectureId}", anyLong()));

		// then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("세션 삭제 성공")
	@Test
	void success_deleteLecture() throws Exception {
		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.delete(DEFAULT_URI + "/{lectureId}", anyLong()));

		// then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("세션 상세 조회 성공")
	@Test
	void success_getLectureDetail() throws Exception {
		// given
		LectureDetailResponseDTO response = lectureDetail();

		doReturn(response).when(lectureService).getLectureDetail(anyLong());

		// when
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.get(DEFAULT_URI + "/detail/{lectureId}", anyLong()));

		// then
		resultActions.andExpect(status().isOk());
	}

	private LectureRequestDTO lectureRequest() {
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

	private long lectureId() {
		return 0L;
	}

	private LecturesResponseDTO lecturesResponse() {
		return new LecturesResponseDTO(LECTURE_GENERATION, lectures());
	}

	private List<LectureVO> lectures() {
		return Stream.iterate(1, i -> i + 1).limit(LIST_SIZE)
				.map(this::lecture).toList();
	}

	private LectureVO lecture(int i) {
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

	private LectureResponseDTO lectureResponse() {
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

	private AttendancesStatusVO attendancesStatus() {
		return AttendancesStatusVO.builder()
				.attendance(80)
				.absent(0)
				.tardy(10)
				.unknown(10)
				.build();
	}

	private List<SubLectureVO> subLectures() {
		return Stream.iterate(1, i -> i + 1).limit(LIST_SIZE)
				.map(this::subLecture).toList();
	}

	private SubLectureVO subLecture(int i) {
		return SubLectureVO.builder()
				.subLectureId((long)i)
				.round(i % 2)
				.startAt(NOW.toString())
				.code(SUB_LECTURE_CODE)
				.build();
	}

	private AttendanceResponseDTO attendanceResponse() {
		return new AttendanceResponseDTO(0L, 0L);
	}

	private AttendanceRequestDTO attendanceRequest() {
		return new AttendanceRequestDTO(0L, 0, SUB_LECTURE_CODE);
	}

	private LectureDetailResponseDTO lectureDetail() {
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
}