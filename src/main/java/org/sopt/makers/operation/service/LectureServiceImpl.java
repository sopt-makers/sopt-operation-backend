package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.repository.AttendanceRepository;
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
		System.out.println(requestDTO.toEntity().getPart());
		Lecture savedLecture = lectureRepository.save(requestDTO.toEntity());
		memberRepository
			.search(getMemberSearchCondition(requestDTO))
			.forEach(member -> attendanceRepository.save(new Attendance(member, savedLecture)));
		return savedLecture.getId();
	}

	private MemberSearchCondition getMemberSearchCondition(LectureRequestDTO requestDTO) {
		return new MemberSearchCondition(
			requestDTO.part() != Part.ALL ? requestDTO.part() : null,
			requestDTO.generation()
		);
	}
}
