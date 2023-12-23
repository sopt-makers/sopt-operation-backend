package org.sopt.makers.operation.repository.schedule;

import org.sopt.makers.operation.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Member, Long>, ScheduleCustomRepository {
}
