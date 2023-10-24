package org.sopt.makers.operation.repository.alarm;

import org.sopt.makers.operation.entity.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmCustomRepository {
}
