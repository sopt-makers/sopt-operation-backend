package org.sopt.makers.operation.client.alarm;

import org.sopt.makers.operation.client.alarm.dto.AlarmSenderRequest;

public interface AlarmSender {
	void send(AlarmSenderRequest request);
}
