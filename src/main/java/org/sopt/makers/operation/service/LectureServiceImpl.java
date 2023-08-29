package org.sopt.makers.operation.service;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.AttendanceStatus.*;
import static org.sopt.makers.operation.entity.lecture.LectureStatus.*;
import static org.sopt.makers.operation.util.Generation32.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import lombok.val;

import org.sopt.makers.operation.dto.lecture.*;

import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.*;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.exception.LectureException;
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
	public LectureGetResponseDTO getCurrentLecture(Long playGroundId) {
		val now = LocalDateTime.now();

		val attendances = attendanceRepository.findCurrentAttendanceByMember(playGroundId);

		if (attendances.isEmpty()) {
			return new LectureGetResponseDTO(LectureResponseType.NO_SESSION, 0L, "", "", "", "", "", Collections.emptyList());
		}

		if (attendances.size() > 2) {
			throw new LectureException(INVALID_COUNT_SESSION.getName());
		}

		// 현재 출석과 Lecture 가져오기
		val currentAttendance = getCurrentAttendance(attendances, now);
		val currentLecture = currentAttendance.getLecture();
		val lectureType = getLectureResponseType(currentLecture);

		if (lectureType.equals(LectureResponseType.NO_ATTENDANCE)) {
			val message = "출석 점수가 반영되지 않아요.";
			return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.emptyList());
		}

		val subAttendances = attendanceRepository.findSubAttendanceByAttendanceId(currentAttendance.getId());

		val firstSubLectureAttendance = subAttendances.get(0);
		val secondSubLectureAttendance = subAttendances.get(1);

		val firstSubLectureAttendanceStatus = firstSubLectureAttendance.getStatus();
		val secondSubLectureAttendanceStatus = secondSubLectureAttendance.getStatus();

		val firstSessionStart = firstSubLectureAttendance.getSubLecture().getStartAt();
		val secondSessionStart = secondSubLectureAttendance.getSubLecture().getStartAt();

		val message = (currentLecture.getAttribute() == Attribute.SEMINAR) ? "" : "행사도 참여하고, 출석점수도 받고, 일석이조!";

		// Lecture 시작 전 혹은 1차 출석 시작 전
		if (now.isBefore(currentLecture.getStartDate()) || !nonNull(firstSessionStart)) {
			return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.emptyList());
		}

		// 1차 출석 시작, 2차 출석 시작 전
		if (now.isAfter(firstSessionStart) && !nonNull(secondSessionStart)) {
			// 1차 출석 중 결석인 상태
			if (now.isBefore(firstSessionStart.plusMinutes(10)) && firstSubLectureAttendanceStatus.equals(ABSENT)) {
				return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.emptyList());
			}

			return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.singletonList(firstSubLectureAttendance));
		}

		// 2차 출석 시작 이후
		if (now.isAfter(secondSessionStart)) {
			// 2차 출석 중 결석인 상태
			if (now.isBefore(secondSessionStart.plusMinutes(10)) && secondSubLectureAttendanceStatus.equals(ABSENT)) {
				return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.singletonList(firstSubLectureAttendance));
			}
		}
		return LectureGetResponseDTO.of(lectureType, currentLecture, message, subAttendances);
	}


	@Override
	public LecturesResponseDTO getLecturesByGeneration(int generation, Part part) {
		val lectures = lectureRepository.findLectures(generation, part);
		return LecturesResponseDTO.of(generation, lectures);
	}

	@Override
	public LectureResponseDTO getLecture(Long lectureId) {
		Lecture lecture = findLecture(lectureId);
		return LectureResponseDTO.of(lecture);
	}

	@Override
	@Transactional
	public AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO) {
		Lecture lecture = findLecture(requestDTO.lectureId());

		// 출석 가능 여부 유효성 체크
		if (requestDTO.round() == 2 && lecture.getLectureStatus().equals(BEFORE)) {
			throw new IllegalStateException(NOT_STARTED_PRE_ATTENDANCE.getName());
		} else if (lecture.getLectureStatus().equals(END)) {
			throw new IllegalStateException(END_LECTURE.getName());
		}

		// 출석 세션 상태 업데이트 (시작)
		SubLecture subLecture = lecture.getSubLectures().stream()
			.filter(session -> session.getRound() == requestDTO.round())
			.findFirst()
			.orElseThrow(() -> new IllegalStateException(NO_SUB_LECTURE_EQUAL_ROUND.getName()));
		subLecture.startAttendance(requestDTO.code());

		return new AttendanceResponseDTO(lecture.getId(), subLecture.getId());
	}

	@Override
	@Transactional
	public void finishLecture(Long lectureId) {
		val lecture = findLecture(lectureId);
		val now = LocalDateTime.now();
		if (now.isBefore(lecture.getEndDate())) {
			throw new IllegalStateException(NOT_END_TIME_YET.getName());
		}
		lecture.finish();
	}

	@Override
	public LectureCurrentRoundResponseDTO getCurrentLectureRound(Long lectureId) {
		val now = LocalDateTime.now();
		val today = now.toLocalDate();
		val startOfDay = today.atStartOfDay();
		val endOfDay = LocalDateTime.of(today, LocalTime.MAX);

		val lecture = lectureRepository.findById(lectureId)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));

		val lectureStartDate = lecture.getStartDate();

		if (lectureStartDate.isBefore(startOfDay) || lectureStartDate.isAfter(endOfDay)) {
			throw new LectureException(NO_SESSION.getName());
		}

		val subLectures = lecture.getSubLectures();

		if (subLectures.isEmpty()) {
			throw new LectureException(NOT_STARTED_ATTENDANCE.getName());
		}

		val subLectureComparator = Comparator.comparing(SubLecture::getRound);
		subLectures.sort(subLectureComparator);

		val firstLecture = subLectures.get(0);
		val secondLecture = subLectures.get(1);

		// 1차, 2차 모두 시작 안했을 때
		if(!nonNull(firstLecture.getStartAt()) && !nonNull(secondLecture.getStartAt())) {
			throw new LectureException(NOT_STARTED_ATTENDANCE.getName());
		}

		// 1차만 시작하였을 때
		if (nonNull(firstLecture.getStartAt()) && !nonNull(secondLecture.getStartAt())) {
			// 1차 출석 시작 전일 때
			if (now.isBefore(firstLecture.getStartAt())) {
				throw new LectureException(firstLecture.getRound() + NOT_STARTED_NTH_ATTENDANCE.getName());
			}

			// 1차 출석이 마감되었을 때
			if (now.isAfter(firstLecture.getStartAt().plusMinutes(10))) {
				throw new LectureException(firstLecture.getRound() + ENDED_ATTENDANCE.getName());
			}

			return LectureCurrentRoundResponseDTO.of(firstLecture);
		}

		if (nonNull(firstLecture.getStartAt()) && nonNull(secondLecture.getStartAt())) {
			// 2차 출석 시작 전일 때
			if (now.isBefore(secondLecture.getStartAt())) {
				throw new LectureException(secondLecture.getRound() + NOT_STARTED_NTH_ATTENDANCE.getName());
			}

			// 2차 출석이 마감되었을 때
			if (now.isAfter(secondLecture.getStartAt().plusMinutes(10))) {
				throw new LectureException(secondLecture.getRound() + ENDED_ATTENDANCE.getName());
			}
		}

		return LectureCurrentRoundResponseDTO.of(secondLecture);
	}

	@Override
	@Transactional
	public void deleteLecture(Long lectureId) {
		Lecture lecture = findLecture(lectureId);
		if (lecture.getLectureStatus().equals(END)) {
			revertMemberScore(lecture);
		}
		subAttendanceRepository.deleteAllBySubLectureIn(lecture.getSubLectures());
		subLectureRepository.deleteAllByLecture(lecture);
		attendanceRepository.deleteAllByLecture(lecture);
		lectureRepository.deleteById(lectureId);
	}

	private void revertMemberScore(Lecture lecture) {
		attendanceRepository.findByLecture(lecture).forEach(attendance -> {
			float score = getUpdateScore(lecture.getAttribute(), attendance.getStatus());
			attendance.getMember().updateScore((-1) * score);
		});
	}

	@Override
	public LectureDetailResponseDTO getLectureDetail(Long lectureId) {
		val lecture = findLecture(lectureId);
		return LectureDetailResponseDTO.of(lecture);
	}

	private MemberSearchCondition getMemberSearchCondition(LectureRequestDTO requestDTO) {
		return new MemberSearchCondition(
			requestDTO.part() != Part.ALL ? requestDTO.part() : null,
			requestDTO.generation()
		);
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
			.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));
	}

	private Attendance getCurrentAttendance(List<Attendance> attendances, LocalDateTime now) {
		val lectureSize = attendances.size();
		val currentHour = now.getHour();

		val attendanceIndex = (lectureSize == 2 && currentHour >= 16) ? 1 : 0;
		return attendances.get(attendanceIndex);
	}

	private LectureResponseType getLectureResponseType(Lecture currentLecture) {
		return (currentLecture.getAttribute() != Attribute.ETC) ? LectureResponseType.HAS_ATTENDANCE : LectureResponseType.NO_ATTENDANCE;
	}
}
