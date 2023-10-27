package org.sopt.makers.operation.entity.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
	NOTICE("공지"),
	NEWS("소식");

	private final String name;
}
