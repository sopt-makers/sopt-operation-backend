package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.response.LectureCurrentRoundResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.LectureDetailResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.TodayLectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.request.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.response.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.request.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.response.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.response.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Part;

public interface LectureService {

	/** WEB **/
	long createLecture(LectureRequestDTO requestDTO);
	LecturesResponseDTO getLectures(int generation, Part part);
	LectureResponseDTO getLecture(long lectureId);
	AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO);
	void endLecture(Long lectureId);
	void deleteLecture(Long lectureId);
	LectureDetailResponseDTO getLectureDetail(long lectureId);

	/** SCHEDULER **/
	void endLectures();

	/** APP **/
	TodayLectureResponseDTO getTodayLecture(long memberPlaygroundId);
	LectureCurrentRoundResponseDTO getCurrentLectureRound(long lectureId);
}
