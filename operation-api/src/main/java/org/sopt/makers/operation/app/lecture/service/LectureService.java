package org.sopt.makers.operation.app.lecture.service;

import org.sopt.makers.operation.app.lecture.dto.response.LectureCurrentRoundResponse;
import org.sopt.makers.operation.app.lecture.dto.response.TodayLectureResponse;

public interface LectureService {

	TodayLectureResponse getTodayLecture(long memberPlaygroundId);
	LectureCurrentRoundResponse getCurrentLectureRound(long lectureId);
}
