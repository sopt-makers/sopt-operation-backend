package org.sopt.makers.operation.service.app.lecture;

import org.sopt.makers.operation.dto.lecture.response.LectureCurrentRoundResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.TodayLectureResponseDTO;

public interface LectureService {

	TodayLectureResponseDTO getTodayLecture(long memberPlaygroundId);
	LectureCurrentRoundResponseDTO getCurrentLectureRound(long lectureId);
}
