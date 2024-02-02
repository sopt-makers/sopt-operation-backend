package org.operation.schedule.repository;

import org.operation.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Member, Long>, ScheduleCustomRepository {
}
