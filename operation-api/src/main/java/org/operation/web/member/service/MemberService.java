package org.operation.web.member.service;

import org.operation.common.domain.Part;
import org.operation.web.member.dto.response.MembersResponse;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MembersResponse getMemberList(Part part, int generation, Pageable pageable);
}
