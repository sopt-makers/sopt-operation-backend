package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;

import java.util.Optional;
import java.util.List;

public interface MemberService {
    Optional<Member> confirmMember(Long playGroundId);
    List<MemberListGetResponse> getMemberList(Part part, int generation);
    public AttendanceTotalResponseDTO getMemberTotalAttendance(Member member);
}
