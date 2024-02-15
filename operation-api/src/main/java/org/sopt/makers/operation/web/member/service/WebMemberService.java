package org.sopt.makers.operation.web.member.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.web.member.dto.response.MemberListGetResponse;
import org.springframework.data.domain.Pageable;

public interface WebMemberService {
    MemberListGetResponse getMembers(Part part, int generation, Pageable pageable);
}
