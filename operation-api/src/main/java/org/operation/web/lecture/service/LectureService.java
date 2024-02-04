package org.operation.web.lecture.service;

import org.operation.common.domain.Part;
import org.operation.web.lecture.dto.request.AttendanceRequest;
import org.operation.web.lecture.dto.request.LectureRequest;
import org.operation.web.lecture.dto.response.AttendanceResponse;
import org.operation.web.lecture.dto.response.LectureDetailResponse;
import org.operation.web.lecture.dto.response.LectureResponse;
import org.operation.web.lecture.dto.response.LecturesResponse;

public interface LectureService {
	long createLecture(LectureRequest request);
	LecturesResponse getLectures(int generation, Part part);
	LectureResponse getLecture(long lectureId);
	AttendanceResponse startAttendance(AttendanceRequest request);
	void endLecture(Long lectureId);
	void deleteLecture(Long lectureId);
	LectureDetailResponse getLectureDetail(long lectureId);
}
