package org.sopt.makers.operation.service;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.util.Generation32.*;

import java.util.List;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendUpdateResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
import lombok.val;
import org.sopt.makers.operation.dto.attendance.*;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final SubLectureRepository subLectureRepository;
	private final AttendanceRepository attendanceRepository;

	@Override
	@Transactional
	public AttendUpdateResponseDTO updateAttendanceStatus(AttendUpdateRequestDTO requestDTO) {
		val subAttendance = findSubAttendance(requestDTO.subAttendanceId());
		val attendance = attendanceRepository.findAttendanceBySubAttendance(subAttendance)
				.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE.getName()));
		subAttendance.updateStatus(requestDTO.status());
		attendance.updateStatus(getAttendanceStatus(requestDTO.attribute(), attendance.getSubAttendances()));
		return AttendUpdateResponseDTO.of(subAttendance);
	}

	@Override
	public AttendanceMemberResponseDTO findAttendancesByMember(Long memberId) {
		val member = findMember(memberId);
		val attendances = attendanceRepository.findAttendancesByMember(memberId);
		return AttendanceMemberResponseDTO.of(member, attendances);
	}

	@Override
	@Transactional
	public float updateMemberScore(Long memberId) {
		Member member = memberRepository.findMemberByIdFetchJoinAttendances(memberId)
			.orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
		member.updateTotalScore();
		return member.getScore();
	}

	@Override
	public List<MemberResponseDTO> findAttendancesByLecture(Long lectureId, Part part) {
		val attendances = attendanceRepository.findAttendancesByLecture(lectureId, part);
		return attendances.stream().map(MemberResponseDTO::of).toList();
	}

	@Override
	@Transactional
	public AttendResponseDTO attend(Long playGroundId, AttendRequestDTO requestDTO) {
		val member = memberRepository.getMemberByPlaygroundId(playGroundId)
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

		attendance.updateStatus(getAttendanceStatus(attendance.getLecture().getAttribute(), attendance.getSubAttendances()));

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
