package org.sopt.makers.operation.domain.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
	NOTICE("공지"),
	NEWS("소식");

	private final String name;
}
