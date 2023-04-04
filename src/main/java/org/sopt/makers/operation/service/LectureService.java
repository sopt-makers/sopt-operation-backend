package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
	LectureResponseDTO getLecturesByGeneration(int generation);
}
