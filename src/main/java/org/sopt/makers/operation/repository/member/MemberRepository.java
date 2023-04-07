package org.sopt.makers.operation.repository.member;

import org.sopt.makers.operation.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Member getMemberByPlaygroundId(Long id);
}
