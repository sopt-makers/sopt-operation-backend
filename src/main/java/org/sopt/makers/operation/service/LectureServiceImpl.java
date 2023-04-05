package org.sopt.makers.operation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.sopt.makers.operation.dto.lecture.AttendanceVO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureVO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

	private final LectureRepository lectureRepository;
	private final SubLectureRepository subLectureRepository;
	private final AttendanceRepository attendanceRepository;
	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public Long createLecture(LectureRequestDTO requestDTO) {
		// 세션 생성
		Lecture savedLecture = lectureRepository.save(requestDTO.toEntity());

		// 출석 세션 2개 생성
		Stream.iterate(1, i -> i + 1).limit(2)
			.forEach(i -> subLectureRepository.save(new SubLecture(savedLecture, i)));

		// 출석 생성
		memberRepository
			.search(getMemberSearchCondition(requestDTO))
			.forEach(member -> attendanceRepository.save(new Attendance(member, savedLecture)));

		// 서브 출석 생성
		savedLecture.getAttendances()
			.forEach(attendance -> savedLecture.getSubLectures()
					.forEach(subLecture -> subAttendanceRepository.save(new SubAttendance(attendance, subLecture))));


		return savedLecture.getId();
	}

	@Override
	public LectureResponseDTO getLecturesByGeneration(int generation) {
		List<LectureVO> lectures = lectureRepository.findByGenerationOrderByStartDateDesc(generation)
			.stream().map(this::getLectureVO)
			.toList();
		return LectureResponseDTO.of(generation, lectures);
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
