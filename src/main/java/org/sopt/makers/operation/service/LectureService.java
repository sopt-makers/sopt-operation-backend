package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.TodayLectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.dto.lecture.*;

public interface LectureService {

	/** WEB **/
	long createLecture(LectureRequestDTO requestDTO);
	LecturesResponseDTO getLectures(int generation, Part part);
	LectureResponseDTO getLecture(Long lectureId);
	AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO);
	void endLecture(Long lectureId);
	void deleteLecture(Long lectureId);
	LectureDetailResponseDTO getLectureDetail(Long lectureId);

	/** SCHEDULER **/
	void endLectures();

	/** APP **/
	TodayLectureResponseDTO getTodayLecture(long memberPlaygroundId);
	LectureCurrentRoundResponseDTO getCurrentLectureRound(Long lectureId);
}
