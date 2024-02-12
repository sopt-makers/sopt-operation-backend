package org.sopt.makers.operation.app.attendance.service;

import org.sopt.makers.operation.app.attendance.dto.request.AttendanceRequest;
import org.sopt.makers.operation.app.attendance.dto.response.AttendanceResponse;

public interface AppAttendanceService {
	AttendanceResponse attend(long memberId, AttendanceRequest request);
}
