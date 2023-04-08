package org.sopt.makers.operation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AttendanceStatus {
	ATTENDANCE("출석"), ABSENT("결석"), TARDY("지각"), LEAVE_EARLY("조퇴");

	final String name;
}
