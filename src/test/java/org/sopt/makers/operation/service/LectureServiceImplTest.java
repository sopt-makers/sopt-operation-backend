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
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTest {

	@InjectMocks
	LectureServiceImpl lectureService;

	@Mock
	private LectureRepository lectureRepository;
	@Mock
	private MemberRepository memberRepository;
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
}