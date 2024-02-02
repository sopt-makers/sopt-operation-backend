package org.operation.app.attendance.service;

import org.operation.app.attendance.dto.request.AttendanceRequest;
import org.operation.app.attendance.dto.response.AttendanceResponse;

public interface AttendanceService {
	AttendanceResponse attend(long memberId, AttendanceRequest request);
}
