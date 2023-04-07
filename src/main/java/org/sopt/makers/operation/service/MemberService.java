package org.sopt.makers.operation.service;

import org.sopt.makers.operation.entity.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> confirmMember(Long playGroundId);
}
