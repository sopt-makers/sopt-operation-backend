package org.sopt.makers.operation.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sopt.makers.operation.dto.lecture.*;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Attribute;
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
		LocalDateTime now = LocalDateTime.now();

		List<Lecture> lectures = lectureRepository.searchLecture(lectureSearchCondition);

		// 세션이 없을 때
		if (lectures.isEmpty()) {
			return new LectureGetResponseDTO(LectureResponseType.NO_SESSION, "", "", null, null, Collections.emptyList());
		}

		Lecture currentSession;
		LectureResponseType type;

		// 하루에 세션이 하나일 때, 하루에 세션이 여러개일 때
		if (lectures.size() == 1) {
			currentSession = lectures.get(0);
			type = (currentSession.getAttribute() == Attribute.EVENT) ? LectureResponseType.NO_ATTENDANCE : LectureResponseType.HAS_ATTENDANCE;
		} else {
			int sessionNumber = (now.getHour() < 16) ? 2 : 3;
			type = (sessionNumber == 3) ? LectureResponseType.NO_ATTENDANCE : LectureResponseType.HAS_ATTENDANCE;
			currentSession = lectures.get(sessionNumber - 2);
		}

		if(type.equals(LectureResponseType.NO_ATTENDANCE)) {
			return LectureGetResponseDTO.of(type, currentSession, Collections.emptyList());
		}

		// 출결 가져오기
		Attendance attendance = attendanceRepository.findAttendanceByLectureIdAndMemberId(currentSession.getId(), lectureSearchCondition.memberId());

		List<LectureGetResponseVO> attendances = attendance.getSubAttendances().stream()
				.map(subAttendance -> {
					return LectureGetResponseVO.of(subAttendance.getStatus(), subAttendance.getLastModifiedDate());
				})
				.collect(Collectors.toList());

		return LectureGetResponseDTO.of(type, currentSession, attendances);
	}


	private MemberSearchCondition getMemberSearchCondition(LectureRequestDTO requestDTO) {
		return new MemberSearchCondition(
			requestDTO.part() != Part.ALL ? requestDTO.part() : null,
			requestDTO.generation()
		);
	}
}
