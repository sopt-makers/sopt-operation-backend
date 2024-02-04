package org.sopt.makers.operation.domain.alarm.repository;

import org.sopt.makers.operation.domain.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmCustomRepository {
}
