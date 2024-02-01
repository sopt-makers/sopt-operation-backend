package org.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Part {
	ALL("전체"),
	PLAN("기획"),
	DESIGN("디자인"),
	WEB("웹"),
	ANDROID("안드로이드"),
	IOS("iOS"),
	SERVER("서버");

	private final String name;
}
