package org.sopt.makers.operation.web.attendnace.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.web.attendnace.dto.request.SubAttendanceUpdateRequest;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceListByLectureGetResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceListByMemberGetResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.MemberScoreUpdateResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.SubAttendanceUpdateResponse;
import org.springframework.data.domain.Pageable;

public interface WebAttendanceService {
	SubAttendanceUpdateResponse updateSubAttendance(SubAttendanceUpdateRequest request);
	AttendanceListByMemberGetResponse getAttendancesByMember(long memberId);
	MemberScoreUpdateResponse updateMemberAllScore(long memberId);
	AttendanceListByLectureGetResponse getAttendancesByLecture(long lectureId, Part part, Pageable pageable);
}
