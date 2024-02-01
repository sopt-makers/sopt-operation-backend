package org.sopt.makers.operation.service.app.attendance;

import org.sopt.makers.operation.dto.attendance.response.AttendResponseDTO;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
	AttendResponseDTO attend(Long playGroundId, AttendRequestDTO requestDTO);
}
