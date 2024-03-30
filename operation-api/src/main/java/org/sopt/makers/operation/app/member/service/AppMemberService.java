package org.sopt.makers.operation.app.member.service;

import org.sopt.makers.operation.app.member.dto.response.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.app.member.dto.response.MemberScoreGetResponse;

public interface AppMemberService {
    AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId);
    MemberScoreGetResponse getMemberScore(Long playGroundId);
}
