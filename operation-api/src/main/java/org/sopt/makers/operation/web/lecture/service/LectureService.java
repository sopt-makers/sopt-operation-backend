package org.sopt.makers.operation.web.lecture.service;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.web.lecture.dto.request.AttendanceRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureRequest;
import org.sopt.makers.operation.web.lecture.dto.response.AttendanceResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureDetailResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LecturesResponse;

public interface LectureService {
	long createLecture(LectureRequest request);
	LecturesResponse getLectures(int generation, Part part);
	LectureResponse getLecture(long lectureId);
	AttendanceResponse startAttendance(AttendanceRequest request);
	void endLecture(Long lectureId);
	void deleteLecture(Long lectureId);
	LectureDetailResponse getLectureDetail(long lectureId);
}
