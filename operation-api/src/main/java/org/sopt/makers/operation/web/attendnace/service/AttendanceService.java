package org.sopt.makers.operation.web.attendnace.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.web.attendnace.dto.request.UpdatedSubAttendanceRequest;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceListResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceMemberResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.UpdatedSubAttendanceResponse;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
	UpdatedSubAttendanceResponse updateSubAttendance(UpdatedSubAttendanceRequest request);
	AttendanceMemberResponse findAttendancesByMember(long memberId);
	float updateMemberAllScore(long memberId);
	AttendanceListResponse findAttendancesByLecture(long lectureId, Part part, Pageable pageable);
}
