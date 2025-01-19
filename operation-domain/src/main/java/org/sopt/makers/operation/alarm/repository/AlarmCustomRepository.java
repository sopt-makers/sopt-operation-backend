package org.sopt.makers.operation.alarm.repository;

import java.util.List;

import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.AlarmStatus;
import org.springframework.data.domain.Pageable;

public interface AlarmCustomRepository {
    List<Alarm> findOrderByCreatedDate(Integer generation, AlarmStatus status, Pageable pageable);

    int count(Integer generation, AlarmStatus status);
}
