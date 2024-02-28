package org.sopt.makers.operation.schedule.repository;

import org.sopt.makers.operation.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Member, Long>, ScheduleCustomRepository {
}
