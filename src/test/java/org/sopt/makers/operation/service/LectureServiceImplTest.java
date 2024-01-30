package org.sopt.makers.operation.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.sopt.makers.operation.fixture.LectureFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.lecture.request.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.response.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.LecturesResponseDTO;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.external.alarm.AlarmSender;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;

import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTest {

	@InjectMocks
	LectureServiceImpl lectureService;

	@Mock
	private LectureRepository lectureRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AlarmSender alarmSender;
	@Mock
	ValueConfig valueConfig;

	@DisplayName("세션 목록 조회")
	@Test
	void getLectures() {
		// given
		doReturn(lectureList()).when(lectureRepository).find(anyInt(), any());

		// when
		LecturesResponseDTO response = lectureService.getLectures(LECTURE_GENERATION, LECTURE_PART);

		// then
		assertThat(response.lectures().size(), is(equalTo(LIST_SIZE)));

		// verify
		verify(lectureRepository, times(1)).find(anyInt(), any());
	}

	@DisplayName("세션 단건 조회")
	@Test
	void getLecture() {
		// given
		Lecture lecture = lectureRequest().toEntity();
		long lectureId = lectureId();

		doReturn(Optional.of(lecture)).when(lectureRepository).findById(anyLong());

		// when
		LectureResponseDTO response = lectureService.getLecture(lectureId);

		// then
		assertThat(response.name(), is(equalTo(lecture.getName())));
		assertThat(response.generation(), is(equalTo(lecture.getGeneration())));
		assertThat(response.part(), is(equalTo(lecture.getPart())));
		assertThat(response.attribute(), is(equalTo(lecture.getAttribute())));

		// verify
		verify(lectureRepository, times(1)).findById(anyLong());
	}

	@DisplayName("출석 시작")
	@Test
	void startAttendance() {
		// given
		Lecture lecture = lectureRequest().toEntity();
		subLecture(lecture);
		AttendanceRequestDTO request = attendanceRequest();

		doReturn(Optional.of(lecture)).when(lectureRepository).findById(anyLong());

		// when
		lectureService.startAttendance(request);

		// verify
		verify(lectureRepository, times(1)).findById(anyLong());
	}

	@DisplayName("세션 종료")
	@Test
	void endLecture() {
		// given
		long lectureId = lectureId();
		Lecture lecture = lectureEnd();

		doReturn(Optional.of(lecture)).when(lectureRepository).findById(anyLong());

		// when
		lectureService.endLecture(lectureId);

		// verify
		verify(lectureRepository, times(1)).findById(anyLong());
	}
}