package org.sopt.makers.operation.controller.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureDetailResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.service.LectureService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.*;
import static org.sopt.makers.operation.fixture.LectureFixture.*;
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

		doReturn(response).when(lectureService).getLectures(anyInt(), any());

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
}