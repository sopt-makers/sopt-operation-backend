package org.sopt.makers.operation.service.app.member;

import org.sopt.makers.operation.dto.attendance.response.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.member.response.MemberScoreGetResponse;

public interface MemberService {
    AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId);
    MemberScoreGetResponse getMemberScore(Long playGroundId);
}
