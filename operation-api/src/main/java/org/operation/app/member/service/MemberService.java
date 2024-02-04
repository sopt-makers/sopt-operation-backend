package org.operation.app.member.service;

import org.operation.app.member.dto.response.AttendanceTotalResponseDTO;
import org.operation.app.member.dto.response.MemberScoreGetResponse;

public interface MemberService {
    AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId);
    MemberScoreGetResponse getMemberScore(Long playGroundId);
}
