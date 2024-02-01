package org.sopt.makers.operation.repository.member;

import org.operation.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> getMemberByPlaygroundIdAndGeneration(Long id, int generation);
    boolean existsByPlaygroundId(Long id);
}
