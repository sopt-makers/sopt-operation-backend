package org.sopt.makers.operation.domain.schedule.repository;

import org.sopt.makers.operation.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Member, Long>, ScheduleCustomRepository {
}
