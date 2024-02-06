package org.sopt.makers.operation.web.member.service;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.web.member.dto.response.MemberListResponse;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberListResponse getMembers(Part part, int generation, Pageable pageable);
}
