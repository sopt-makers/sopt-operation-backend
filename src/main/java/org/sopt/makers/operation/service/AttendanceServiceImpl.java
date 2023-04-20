package org.sopt.makers.operation.service;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendUpdateResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
import lombok.val;
import org.sopt.makers.operation.dto.attendance.*;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.sopt.makers.operation.util.Generation32;
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
	private final LectureRepository lectureRepository;
	private final SubLectureRepository subLectureRepository;
	private final AttendanceRepository attendanceRepository;
	private final Generation32 sopt32;

	@Override
	@Transactional
	public AttendUpdateResponseDTO updateAttendanceStatus(AttendUpdateRequestDTO requestDTO) {
		val subAttendance = findSubAttendance(requestDTO.subAttendanceId());
		subAttendance.updateStatus(requestDTO.status());

		val attendance = subAttendance.getAttendance();
		attendance.updateStatus(sopt32.getAttendanceStatus(requestDTO.attribute(), attendance.getSubAttendances()));

		return AttendUpdateResponseDTO.of(subAttendance);
	}

	@Override
	public AttendanceMemberResponseDTO findMemberAttendance(Long memberId) {
		val member = findMember(memberId);
		val attendances = attendanceRepository.findByMember(member);

		HashMap<Long, ArrayList<MemberInfo>> map = new HashMap<>();
		for (MemberInfo info : attendances) {
			Long id = info.attendanceId();
			if (!map.containsKey(id)) map.put(id, new ArrayList<>());
			map.get(id).add(info);
		}

		return AttendanceMemberResponseDTO.of(member, map);
	}

	@Override
	@Transactional
	public float updateMemberScore(Long memberId) {
		val member = findMember(memberId);
		val score = (float)(2 + attendanceRepository.findAttendancesOfMember(member)
			.stream()
			.mapToDouble(info -> sopt32.getUpdateScore(info.attribute(), info.status()))
			.sum());
		member.setScore(score);
		return member.getScore();
	}

	@Override
	public List<MemberResponseDTO> getMemberAttendances(Long lectureId, Part part, Pageable pageable) {
		Lecture lecture = findLecture(lectureId);
		List<Attendance> attendances = attendanceRepository.findLectureAttendances(lecture, part, pageable);
		return attendances.stream().map(attendance ->
			MemberResponseDTO.of(
				attendance,
				sopt32.getUpdateScore(lecture.getAttribute(), attendance.getStatus())
			)
		).toList();
	}

	@Override
	@Transactional
	public AttendResponseDTO attend(Long playGroundId, AttendRequestDTO requestDTO) {
		val memberId = memberRepository.getMemberByPlaygroundId(playGroundId).getId();

		val now = LocalDateTime.now();

		val subLecture = subLectureRepository.findById(requestDTO.subLectureId())
				.orElseThrow(() -> new EntityNotFoundException(INVALID_SUB_LECTURE.getName()));

		if(!nonNull(subLecture.getStartAt()) || !nonNull(subLecture.getCode())) throw new LectureException(NOT_STARTED_ATTENDANCE.getName());

		val currentRound = subLecture.getRound();

		if(!subLecture.getCode().equals(requestDTO.code())) throw new SubLectureException(INVALID_CODE.getName());

		if(now.isBefore(subLecture.getStartAt())) throw new LectureException(subLecture.getRound() + NOT_STARTED_NTH_ATTENDANCE.getName());

		if(now.isAfter(subLecture.getStartAt().plusMinutes(10))) throw new LectureException(subLecture.getRound() + ENDED_ATTENDANCE.getName());

		Attendance attendance = attendanceRepository.findAttendanceByLectureIdAndMemberId(subLecture.getLecture().getId(), memberId);

		val currentRoundSubAttendance = attendance.getSubAttendances()
      		.stream()
			.filter(subAttendance ->
					(subAttendance.getSubLecture().getRound() == currentRound)
			).findFirst();

		currentRoundSubAttendance.get().updateStatus(AttendanceStatus.ATTENDANCE);

		attendance.updateStatus(sopt32.getAttendanceStatus(attendance.getLecture().getAttribute(), attendance.getSubAttendances()));
		this.updateMemberScore(memberId);

		return AttendResponseDTO.of(subLecture.getId());
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
	}

	private SubAttendance findSubAttendance(Long id) {
		return subAttendanceRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ATTENDANCE.getName()));
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
			.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));
	}
}
