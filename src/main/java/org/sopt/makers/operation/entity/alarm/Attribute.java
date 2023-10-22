package org.sopt.makers.operation.entity.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
	NOTIFICATION("공지"),
	NEWS("소식");

	private final String name;
}
