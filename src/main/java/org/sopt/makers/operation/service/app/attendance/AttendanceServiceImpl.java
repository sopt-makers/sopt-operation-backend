package org.sopt.makers.operation.service.app.attendance;

import static java.util.Objects.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import java.time.LocalDateTime;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.attendance.response.AttendResponseDTO;
import org.sopt.makers.operation.entity.attendance.AttendanceStatus;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final MemberRepository memberRepository;
	private final SubLectureRepository subLectureRepository;
	private final AttendanceRepository attendanceRepository;
	private final ValueConfig valueConfig;

	@Override
	@Transactional
	public AttendResponseDTO attend(Long playGroundId, AttendRequestDTO requestDTO) {
		log.info("[Attendance: attend start] id: " + playGroundId);
		val member = memberRepository.getMemberByPlaygroundIdAndGeneration(playGroundId, valueConfig.getGENERATION())
				.orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

		val memberId = member.getId();

		val now = LocalDateTime.now();

		val subLecture = subLectureRepository.findById(requestDTO.subLectureId())
				.orElseThrow(() -> new SubLectureException(INVALID_SUB_LECTURE.getName()));

		if (!nonNull(subLecture.getStartAt()) || !nonNull(subLecture.getCode())) {
			throw new LectureException(NOT_STARTED_ATTENDANCE.getName());
		}

		val currentRound = subLecture.getRound();

		if (!subLecture.getCode().equals(requestDTO.code())) {
			throw new SubLectureException(INVALID_CODE.getName());
		}

		if (now.isBefore(subLecture.getStartAt())) {
			throw new LectureException(subLecture.getRound() + NOT_STARTED_NTH_ATTENDANCE.getName());
		}

		if (now.isAfter(subLecture.getStartAt().plusMinutes(10))) {
			throw new LectureException(subLecture.getRound() + ENDED_ATTENDANCE.getName());
		}

		val attendance = attendanceRepository.findAttendanceByLectureIdAndMemberId(subLecture.getLecture().getId(), memberId)
				.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE.getName()));

		val currentRoundSubAttendance = attendance.getSubAttendances()
      		.stream()
			.filter(subAttendance -> subAttendance.getSubLecture().getRound() == currentRound)
			.findFirst()
			.orElseThrow(() -> new SubLectureException(INVALID_SUB_ATTENDANCE.getName()));

		currentRoundSubAttendance.updateStatus(AttendanceStatus.ATTENDANCE);

		log.info("[Attendance:currentRound update] id: " + playGroundId + " subStatus: " + currentRoundSubAttendance.getStatus());

		attendance.updateStatus();

		log.info("[Attendance:attendance update] id: " + playGroundId + " subStatus: " + attendance.getStatus());

		return AttendResponseDTO.of(subLecture.getId());
	}
}
