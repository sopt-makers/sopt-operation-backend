package org.operation.client.alarm;

import org.operation.web.alarm.dto.request.AlarmSenderRequest;

public interface AlarmSender {
	void send(AlarmSenderRequest request);
}
