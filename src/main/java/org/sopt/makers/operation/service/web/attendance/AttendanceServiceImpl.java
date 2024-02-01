package org.sopt.makers.operation.service.web.attendance;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import org.sopt.makers.operation.dto.attendance.request.SubAttendanceUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendancesResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.SubAttendanceUpdateResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.operation.attendance.SubAttendance;
import org.operation.member.Member;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.attendance.SubAttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.data.domain.Pageable;
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

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final AttendanceRepository attendanceRepository;

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

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
	}

	private SubAttendance findSubAttendance(Long id) {
		return subAttendanceRepository.findById(id)
			.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE.getName()));
	}
}
