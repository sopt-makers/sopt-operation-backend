package org.sopt.makers.operation.repository;

import org.sopt.makers.operation.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
