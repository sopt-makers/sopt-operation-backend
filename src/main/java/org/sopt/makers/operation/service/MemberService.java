package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.dto.member.MemberRequestDTO;
import org.sopt.makers.operation.dto.member.MemberScoreGetResponse;
import org.sopt.makers.operation.entity.Part;

import java.util.List;

public interface MemberService {
    List<MemberListGetResponse> getMemberList(Part part, int generation);
    AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId);
    MemberScoreGetResponse getMemberScore(Long playGroundId);
    void putMember(Long playGroundId, MemberRequestDTO requestDTO);
}
