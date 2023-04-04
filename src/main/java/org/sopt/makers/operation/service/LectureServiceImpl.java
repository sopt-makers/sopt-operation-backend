package org.sopt.makers.operation.service;

import java.util.List;

import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.repository.AttendanceRepository;
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
		for (int i = 0; i < 2; i++) {
			subLectureRepository.save(new SubLecture(savedLecture, i + 1));
		}

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

	private MemberSearchCondition getMemberSearchCondition(LectureRequestDTO requestDTO) {
		return new MemberSearchCondition(
			requestDTO.part() != Part.ALL ? requestDTO.part() : null,
			requestDTO.generation()
		);
	}
}
