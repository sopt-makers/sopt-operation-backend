package org.sopt.makers.operation.app.attendance.service;

import org.sopt.makers.operation.app.attendance.dto.request.LectureAttendRequest;
import org.sopt.makers.operation.app.attendance.dto.response.LectureAttendResponse;

public interface AppAttendanceService {
	LectureAttendResponse attend(long memberId, LectureAttendRequest request);
}
