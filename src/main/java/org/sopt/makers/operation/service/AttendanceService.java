package org.sopt.makers.operation.service;

<<<<<<< HEAD
import java.util.List;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;
=======
import org.sopt.makers.operation.dto.attendance.*;
>>>>>>> develop

public interface AttendanceService {
	AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO);
	AttendanceMemberResponseDTO getMemberAttendance(Long memberId);
	float updateMemberScore(Long memberId);
<<<<<<< HEAD
	List<MemberResponseDTO> getMemberAttendances(Long lectureId, Part part, Pageable pageable);
=======
	AttendResponseDTO attend(Long memberId, AttendRequestDTO requestDTO);
>>>>>>> develop
}
