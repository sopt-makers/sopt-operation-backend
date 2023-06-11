package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.dto.lecture.*;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LectureGetResponseDTO getCurrentLecture(Long playGroundId);
	LecturesResponseDTO getLecturesByGeneration(int generation, Part part);
	LectureResponseDTO getLecture(Long lectureId);
	AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO);
	void finishLecture(Long lectureId);
	LectureCurrentRoundResponseDTO getCurrentLectureRound(Long lectureId);
	void deleteLecture(Long lectureId);

}
