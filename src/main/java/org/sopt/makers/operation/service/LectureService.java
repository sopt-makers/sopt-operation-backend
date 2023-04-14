package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.*;
import org.sopt.makers.operation.entity.Part;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LectureGetResponseDTO getCurrentLecture(LectureSearchCondition lectureSearchCondition);
	LecturesResponseDTO getLecturesByGeneration(int generation);
	LectureResponseDTO getLecture(Long lectureId, Part part);
	AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO);
	void updateMembersScore(Long lectureId);
	LectureCurrentRoundResponseDTO getCurrentLectureRound(Long lectureId);

}
