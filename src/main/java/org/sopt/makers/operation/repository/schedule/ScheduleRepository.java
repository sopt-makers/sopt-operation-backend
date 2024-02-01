package org.sopt.makers.operation.repository.schedule;

import org.operation.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Member, Long>, ScheduleCustomRepository {
}
