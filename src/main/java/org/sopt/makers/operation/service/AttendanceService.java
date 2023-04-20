package org.sopt.makers.operation.service;

import java.util.List;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendUpdateResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;
import org.sopt.makers.operation.dto.attendance.*;

public interface AttendanceService {
	AttendUpdateResponseDTO updateAttendanceStatus(AttendUpdateRequestDTO requestDTO);
	AttendanceMemberResponseDTO findMemberAttendance(Long memberId);
	float updateMemberScore(Long memberId);
	List<MemberResponseDTO> getMemberAttendances(Long lectureId, Part part, Pageable pageable);
	AttendResponseDTO attend(Long memberId, AttendRequestDTO requestDTO);
}
