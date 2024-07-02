package org.sopt.makers.operation.alarm.repository;

import java.util.List;
import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.Status;
import org.springframework.data.domain.Pageable;

public interface AlarmCustomRepository {
    List<Alarm> findOrderByCreatedDate(Integer generation, Status status, Pageable pageable);

    int count(Integer generation, Status status);
}
