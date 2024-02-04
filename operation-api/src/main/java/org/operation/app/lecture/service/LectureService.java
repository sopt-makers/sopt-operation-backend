package org.operation.app.lecture.service;

import org.operation.app.lecture.dto.response.LectureCurrentRoundResponse;
import org.operation.app.lecture.dto.response.TodayLectureResponse;

public interface LectureService {

	TodayLectureResponse getTodayLecture(long memberPlaygroundId);
	LectureCurrentRoundResponse getCurrentLectureRound(long lectureId);
}
