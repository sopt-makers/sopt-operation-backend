package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.dto.member.MemberRequestDTO;
import org.sopt.makers.operation.dto.member.MemberScoreGetResponse;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    List<MemberListGetResponse> getMemberList(Part part, int generation, Pageable pageable);
    AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId);
    MemberScoreGetResponse getMemberScore(Long playGroundId);
    void createMember(MemberRequestDTO requestDTO);
}
