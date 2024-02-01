package org.operation.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AttendanceStatus {
	ATTENDANCE("출석"), ABSENT("결석"), TARDY("지각"), LEAVE_EARLY("조퇴"), PARTICIPATE("참여"), NOT_PARTICIPATE("미참여");

	final String name;
}
