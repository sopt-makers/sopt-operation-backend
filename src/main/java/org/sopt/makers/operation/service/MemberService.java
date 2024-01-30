package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.response.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.member.request.MemberRequestDTO;
import org.sopt.makers.operation.dto.member.response.MemberScoreGetResponse;
import org.sopt.makers.operation.dto.member.response.MembersResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MembersResponseDTO getMemberList(Part part, int generation, Pageable pageable);
    AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId);
    MemberScoreGetResponse getMemberScore(Long playGroundId);
    void createMember(MemberRequestDTO requestDTO);
}
