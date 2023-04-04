package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.lecture.AttendanceVO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureVO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

	private final LectureRepository lectureRepository;
	private final AttendanceRepository attendanceRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public Long createLecture(LectureRequestDTO requestDTO) {
		Lecture savedLecture = lectureRepository.save(requestDTO.toEntity());
		memberRepository
			.search(getMemberSearchCondition(requestDTO))
			.forEach(member -> attendanceRepository.save(new Attendance(member, savedLecture)));
		return savedLecture.getId();
	}

	@Override
	public LecturesResponseDTO getLecturesByGeneration(int generation) {
		List<LectureVO> lectures = lectureRepository.findByGenerationOrderByStartDateDesc(generation)
			.stream().map(this::getLectureVO)
			.toList();
		return LecturesResponseDTO.of(generation, lectures);
	}

	@Override
	public LectureResponseDTO getLecture(Long lectureId, Part part) {
		System.out.println("test: " + lectureId + " " + part);
		Lecture lecture = lectureRepository.findById(lectureId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_LECTURE.getName()));
		List<Attendance> attendances = attendanceRepository.getAttendanceByPart(lecture, part);
		return LectureResponseDTO.of(lecture, attendances);
	}

	private LectureVO getLectureVO(Lecture lecture) {
		return LectureVO.of(lecture, getAttendanceVO(lecture));
	}

	private AttendanceVO getAttendanceVO(Lecture lecture) {
		if (lecture.getEndDate().isBefore(LocalDateTime.now())) {
			return new AttendanceVO(
				attendanceRepository.countAttendance(lecture),
				attendanceRepository.countAbsent(lecture),
				attendanceRepository.countTardy(lecture),
				0L);
		} else {
			return new AttendanceVO(
				attendanceRepository.countAttendance(lecture),
				0L,
				attendanceRepository.countTardy(lecture),
				attendanceRepository.countAbsent(lecture)
				);
		}
	}

	private MemberSearchCondition getMemberSearchCondition(LectureRequestDTO requestDTO) {
		return new MemberSearchCondition(
			requestDTO.part() != Part.ALL ? requestDTO.part() : null,
			requestDTO.generation()
		);
	}
}
