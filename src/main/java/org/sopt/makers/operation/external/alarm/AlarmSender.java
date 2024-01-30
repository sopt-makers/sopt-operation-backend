package org.sopt.makers.operation.external.alarm;

import org.sopt.makers.operation.dto.alarm.request.AlarmSenderDTO;

public interface AlarmSender {
	void send(AlarmSenderDTO alarmSenderDTO);
}
