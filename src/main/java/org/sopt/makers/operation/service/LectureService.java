package org.sopt.makers.operation.service;

<<<<<<< HEAD
import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
=======
import org.sopt.makers.operation.dto.lecture.*;
import org.sopt.makers.operation.entity.Part;
>>>>>>> develop

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LectureGetResponseDTO getCurrentLecture(LectureSearchCondition lectureSearchCondition);
	LecturesResponseDTO getLecturesByGeneration(int generation);
	LectureResponseDTO getLecture(Long lectureId);
	AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO);
	void updateMembersScore(Long lectureId);
	LectureCurrentRoundResponseDTO getCurrentLectureRound(Long lectureId);

}
