package org.sopt.makers.operation.domain.alarm.repository;

import java.util.List;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.alarm.domain.Alarm;
import org.sopt.makers.operation.domain.alarm.domain.Status;
import org.springframework.data.domain.Pageable;

public interface AlarmCustomRepository {
	List<Alarm> findOrderByCreatedDate(Integer generation, Part part, Status status, Pageable pageable);
	int count(int generation, Part part, Status status);
}
