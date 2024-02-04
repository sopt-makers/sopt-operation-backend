package org.operation.alarm.repository;

import java.util.List;

import org.operation.alarm.domain.Alarm;
import org.operation.alarm.domain.Status;
import org.operation.common.domain.Part;
import org.springframework.data.domain.Pageable;

public interface AlarmCustomRepository {
	List<Alarm> getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	int countByGenerationAndPartAndStatus(int generation, Part part, Status status);
}
