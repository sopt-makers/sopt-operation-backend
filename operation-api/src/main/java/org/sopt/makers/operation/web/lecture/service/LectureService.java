package org.sopt.makers.operation.web.lecture.service;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.web.lecture.dto.request.AttendanceRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureRequest;
import org.sopt.makers.operation.web.lecture.dto.response.AttendanceResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureDetailResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureListResponse;

public interface LectureService {
	long createLecture(LectureRequest request);
	LectureListResponse getLectures(int generation, Part part);
	LectureResponse getLecture(long lectureId);
	AttendanceResponse startAttendance(AttendanceRequest request);
	void endLecture(long lectureId);
	void endLectures();
	void deleteLecture(long lectureId);
	LectureDetailResponse getLectureDetail(long lectureId);
}
