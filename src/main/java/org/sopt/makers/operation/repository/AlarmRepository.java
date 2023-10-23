package org.sopt.makers.operation.repository;

import org.sopt.makers.operation.entity.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Optional<Alarm> findById(Long alarmId);
}

