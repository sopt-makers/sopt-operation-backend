package org.sopt.makers.operation.client.alarm;

import org.sopt.makers.operation.web.alarm.dto.request.AlarmSenderRequest;

public interface AlarmSender {
	void send(AlarmSenderRequest request);
}
