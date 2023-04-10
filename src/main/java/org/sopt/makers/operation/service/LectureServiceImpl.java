package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalCountVO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalVO;
import org.sopt.makers.operation.dto.lecture.*;
import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceVO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureVO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.*;
import org.sopt.makers.operation.entity.lecture.Attribute;
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
	public LectureGetResponseDTO getCurrentLecture(LectureSearchCondition lectureSearchCondition) {
		LocalDateTime now = LocalDateTime.now();

		List<Lecture> lectures = lectureRepository.searchLecture(lectureSearchCondition);

		// 세션이 없을 때
		if (lectures.isEmpty()) {
			return new LectureGetResponseDTO(LectureResponseType.NO_SESSION, "", "", "", "", Collections.emptyList());
		}

		Lecture currentSession;
		LectureResponseType type;

		// 하루에 세션이 하나일 때, 하루에 세션이 여러개일 때
		if (lectures.size() == 1) {
			currentSession = lectures.get(0);
			type = (currentSession.getAttribute() == Attribute.SEMINAR) ? LectureResponseType.HAS_ATTENDANCE : LectureResponseType.NO_ATTENDANCE;
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
				.map(subAttendance -> LectureGetResponseVO.of(subAttendance.getStatus(), subAttendance.getLastModifiedDate()))
				.collect(Collectors.toList());

		return LectureGetResponseDTO.of(type, currentSession, attendances);
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
		Lecture lecture = lectureRepository.findById(lectureId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_LECTURE.getName()));
		List<Attendance> attendances = attendanceRepository.getAttendanceByPart(lecture, part);
		return LectureResponseDTO.of(lecture, attendances);
	}

	@Override
	@Transactional
	public AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO) {
		Lecture lecture = lectureRepository.findById(requestDTO.lectureId())
			.orElseThrow(() -> new EntityNotFoundException(INVALID_LECTURE.getName()));

		for (SubLecture subLecture : lecture.getSubLectures()) {
			if (subLecture.getRound() < requestDTO.round() && subLecture.getStartAt() == null) {
				throw new IllegalStateException(NOT_STARTED_PRE_ATTENDANCE.getName());
			} else if (subLecture.getRound() == requestDTO.round()) {
				subLecture.startAttendance();
				return new AttendanceResponseDTO(lecture.getId(), subLecture.getId());
			}
		}

		throw new IllegalStateException(INVALID_LECTURE.getName());
	}

	@Override
	public AttendanceTotalResponseDTO getTotal(Member member) {
		List<AttendanceTotalVO> attendances = attendanceRepository.findAttendanceByMemberId(member.getId())
			.stream().map(this::getTotalAttendanceVO)
			.toList();

		Map<AttendanceStatus, Integer> countAttendance = attendances.stream()
			.map(this::getAttendanceStatus)
			.collect(
				() -> new EnumMap<>(AttendanceStatus.class),
				(map, status) -> map.merge(status, 1, Integer::sum),
				(map1, map2) -> map2.forEach((status, count) -> map1.merge(status, count, Integer::sum))
			);

		AttendanceTotalCountVO total = AttendanceTotalCountVO.of(
			countAttendance.size(),
			countAttendance.getOrDefault(AttendanceStatus.ATTENDANCE, 0),
			countAttendance.getOrDefault(AttendanceStatus.ABSENT, 0),
			countAttendance.getOrDefault(AttendanceStatus.TARDY, 0)
		);

		return AttendanceTotalResponseDTO.of(member, total, attendances);
	}

	private AttendanceTotalVO getTotalAttendanceVO(Attendance attendance) {
		return AttendanceTotalVO.of(attendance);
	}

	private AttendanceStatus getAttendanceStatus(AttendanceTotalVO attendance) {
		return attendance.status();
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
