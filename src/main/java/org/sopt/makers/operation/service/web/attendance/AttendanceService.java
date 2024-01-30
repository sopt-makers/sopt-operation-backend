package org.sopt.makers.operation.service.web.attendance;

import org.sopt.makers.operation.dto.attendance.request.SubAttendanceUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendancesResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.SubAttendanceUpdateResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
	SubAttendanceUpdateResponseDTO updateSubAttendance(SubAttendanceUpdateRequestDTO requestDTO);
	AttendanceMemberResponseDTO findAttendancesByMember(Long memberId);
	float updateMemberScore(Long memberId);
	AttendancesResponseDTO findAttendancesByLecture(Long lectureId, Part part, Pageable pageable);
}
