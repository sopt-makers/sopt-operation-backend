package org.sopt.makers.operation.web.member.service;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.web.member.dto.response.MembersResponse;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MembersResponse getMemberList(Part part, int generation, Pageable pageable);
}
