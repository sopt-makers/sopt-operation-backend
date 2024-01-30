package org.sopt.makers.operation.service.app.attendance;

import org.sopt.makers.operation.dto.attendance.request.AttendRequestDTO;
import org.sopt.makers.operation.dto.attendance.request.SubAttendanceUpdateRequestDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.AttendancesResponseDTO;
import org.sopt.makers.operation.dto.attendance.response.SubAttendanceUpdateResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
	AttendResponseDTO attend(Long playGroundId, AttendRequestDTO requestDTO);
}
