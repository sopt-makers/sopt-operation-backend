package org.sopt.makers.operation.member.repository;

import java.util.Optional;

import org.sopt.makers.operation.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    //Optional<Member> getMemberByPlaygroundIdAndGeneration(Long id, int generation);

    Optional<Member> getMemberByIdAndGeneration(Long memberId, int generation);  // 새 메서드 추가 ⭐️

}