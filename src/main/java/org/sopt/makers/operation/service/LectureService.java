package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LectureGetResponseDTO getCurrentLecture(LectureSearchCondition lectureSearchCondition);
}
