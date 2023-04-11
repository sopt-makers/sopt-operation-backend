package org.sopt.makers.operation.repository.member;

import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Member getMemberByPlaygroundId(Long id);

    List<Member> getMembersByPartAndGeneration(Part part, int generation);
}
