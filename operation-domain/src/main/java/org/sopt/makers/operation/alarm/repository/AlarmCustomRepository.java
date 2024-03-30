package org.sopt.makers.operation.alarm.repository;

import java.util.List;

import org.sopt.makers.operation.alarm.domain.Alarm;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.common.domain.Part;
import org.springframework.data.domain.Pageable;

public interface AlarmCustomRepository {
	List<Alarm> findOrderByCreatedDate(Integer generation, Part part, Status status, Pageable pageable);
	int count(int generation, Part part, Status status);
}
