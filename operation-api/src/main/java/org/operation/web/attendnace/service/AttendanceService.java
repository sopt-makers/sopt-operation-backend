package org.operation.web.attendnace.service;

import org.operation.common.domain.Part;
import org.operation.web.attendance.dto.request.SubAttendanceUpdateRequest;
import org.operation.web.attendance.dto.response.AttendanceMemberResponse;
import org.operation.web.attendance.dto.response.AttendancesResponse;
import org.operation.web.attendance.dto.response.SubAttendanceUpdateResponse;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
	SubAttendanceUpdateResponse updateSubAttendance(SubAttendanceUpdateRequest request);
	AttendanceMemberResponse findAttendancesByMember(long memberId);
	float updateMemberScore(Long memberId);
	AttendancesResponse findAttendancesByLecture(long lectureId, Part part, Pageable pageable);
}
