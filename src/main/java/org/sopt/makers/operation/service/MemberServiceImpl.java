package org.sopt.makers.operation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> confirmMember(Long playGroundId) {
        return Optional.ofNullable(memberRepository.getMemberByPlaygroundId(playGroundId));
    }
}
