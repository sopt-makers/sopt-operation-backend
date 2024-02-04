package org.sopt.makers.operation.web.attendnace.service;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.web.attendance.dto.request.SubAttendanceUpdateRequest;
import org.sopt.makers.operation.web.attendance.dto.response.AttendanceMemberResponse;
import org.sopt.makers.operation.web.attendance.dto.response.AttendancesResponse;
import org.sopt.makers.operation.web.attendance.dto.response.SubAttendanceUpdateResponse;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
	SubAttendanceUpdateResponse updateSubAttendance(SubAttendanceUpdateRequest request);
	AttendanceMemberResponse findAttendancesByMember(long memberId);
	float updateMemberScore(Long memberId);
	AttendancesResponse findAttendancesByLecture(long lectureId, Part part, Pageable pageable);
}
