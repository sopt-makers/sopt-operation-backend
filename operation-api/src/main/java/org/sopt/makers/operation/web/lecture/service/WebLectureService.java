package org.sopt.makers.operation.web.lecture.service;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.web.lecture.dto.request.SubLectureStartRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureCreateRequest;
import org.sopt.makers.operation.web.lecture.dto.response.SubLectureStartResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureCreateResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureDetailGetResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureGetResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureListGetResponse;

public interface WebLectureService {
	LectureCreateResponse createLecture(LectureCreateRequest request);
	LectureListGetResponse getLectures(int generation, Part part);
	LectureGetResponse getLecture(long lectureId);
	SubLectureStartResponse startSubLecture(SubLectureStartRequest request);
	void endLecture(long lectureId);
	void endLectures();
	void deleteLecture(long lectureId);
	LectureDetailGetResponse getLectureDetail(long lectureId);
}
