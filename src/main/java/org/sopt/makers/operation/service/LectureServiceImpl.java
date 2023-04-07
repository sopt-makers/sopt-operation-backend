package org.sopt.makers.operation.service;

import java.util.List;
import java.util.stream.Stream;

import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.repository.AttendanceRepository;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

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
	public LectureGetResponseDTO getCurrentLecture(LectureSearchCondition lectureSearchCondition) {
		List<Lecture> lectures = lectureRepository.searchLecture(lectureSearchCondition);

		if(lectures.size() == 0) return new LectureGetResponseDTO(1, "", "", null, null, List.of());

		if(lectures.size() > 1) throw new LectureException("세션 조회 실패");

		Lecture currentSession = lectures.get(0);

		if (currentSession.getAttribute().equals(Attribute.EVENT)) {
			return new LectureGetResponseDTO(3, currentSession.getPlace(), currentSession.getName(), currentSession.getStartDate(), currentSession.getEndDate(), List.of());
		}

		//타입 2일때만 출석 정보를 준다 (1차, 2차 출석)
		return new LectureGetResponseDTO(2, currentSession.getPlace(), currentSession.getName(), currentSession.getStartDate(), currentSession.getEndDate(), List.of());
	}

	private MemberSearchCondition getMemberSearchCondition(LectureRequestDTO requestDTO) {
		return new MemberSearchCondition(
			requestDTO.part() != Part.ALL ? requestDTO.part() : null,
			requestDTO.generation()
		);
	}
}
