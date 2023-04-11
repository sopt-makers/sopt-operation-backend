package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LectureGetResponseDTO getCurrentLecture(LectureSearchCondition lectureSearchCondition);
	LecturesResponseDTO getLecturesByGeneration(int generation);
	LectureResponseDTO getLecture(Long lectureId, Part part);
	AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO);
	AttendanceTotalResponseDTO getTotal(Member member);
	void updateMembersScore(Long lectureId);

}
