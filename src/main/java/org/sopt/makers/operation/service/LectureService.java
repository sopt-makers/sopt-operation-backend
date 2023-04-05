package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Part;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LecturesResponseDTO getLecturesByGeneration(int generation);
	LectureResponseDTO getLecture(Long lectureId, Part part);
}
