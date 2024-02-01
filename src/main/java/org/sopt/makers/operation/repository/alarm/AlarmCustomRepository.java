package org.sopt.makers.operation.repository.alarm;

import java.util.List;

import org.sopt.makers.operation.entity.Part;
import org.operation.alarm.Alarm;
import org.operation.alarm.Status;
import org.springframework.data.domain.Pageable;

public interface AlarmCustomRepository {
	List<Alarm> getAlarms(Integer generation, Part part, Status status, Pageable pageable);
	int countByGenerationAndPartAndStatus(int generation, Part part, Status status);
}
