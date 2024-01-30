package org.sopt.makers.operation.service;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.attendance.request.AttendRequestDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.request.SubAttendanceUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendancesResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.SubAttendanceUpdateResponseDTO;
import lombok.val;

import org.sopt.makers.operation.entity.attendance.AttendanceStatus;
import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.attendance.SubAttendance;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.repository.attendance.SubAttendanceRepository;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final SubLectureRepository subLectureRepository;
	private final AttendanceRepository attendanceRepository;
	private final ValueConfig valueConfig;

	@Override
	@Transactional
	public SubAttendanceUpdateResponseDTO updateSubAttendance(SubAttendanceUpdateRequestDTO requestDTO) {
		val subAttendance = findSubAttendance(requestDTO.subAttendanceId());
		subAttendance.updateStatus(requestDTO.status());
		return SubAttendanceUpdateResponseDTO.of(subAttendance);
	}

	@Override
	public AttendanceMemberResponseDTO findAttendancesByMember(Long memberId) {
		val member = findMember(memberId);
		val attendances = attendanceRepository.findByMember(member);
		return AttendanceMemberResponseDTO.of(member, attendances);
	}

	@Override
	@Transactional
	public float updateMemberScore(Long memberId) {
		Member member = memberRepository.find(memberId)
			.orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
		member.updateTotalScore();
		return member.getScore();
	}

	@Override
	public AttendancesResponseDTO findAttendancesByLecture(Long lectureId, Part part, Pageable pageable) {
		val attendances = attendanceRepository.findByLecture(lectureId, part, pageable);
		val attendancesCount = attendanceRepository.countByLectureIdAndPart(lectureId, part);
		return AttendancesResponseDTO.of(attendances, attendancesCount);
	}

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

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
	}

	private SubAttendance findSubAttendance(Long id) {
		return subAttendanceRepository.findById(id)
			.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE.getName()));
	}
}
