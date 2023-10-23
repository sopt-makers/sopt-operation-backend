package org.sopt.makers.operation.entity.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
	BEFORE("발송 전"),
	AFTER("발송 후");

	private final String name;
}
