package org.sopt.makers.operation.external.api;

import org.sopt.makers.operation.dto.alarm.AlarmSenderDTO;

public interface AlarmSender {
	void send(AlarmSenderDTO alarmSenderDTO);
}
