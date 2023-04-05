package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;

public interface LectureService {
	Long createLecture(LectureRequestDTO requestDTO);
}
