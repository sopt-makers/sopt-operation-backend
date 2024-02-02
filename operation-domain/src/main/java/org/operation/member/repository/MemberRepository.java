package org.operation.member.repository;

import java.util.Optional;

import org.operation.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> getMemberByPlaygroundIdAndGeneration(Long id, int generation);
    boolean existsByPlaygroundId(Long id);
}
