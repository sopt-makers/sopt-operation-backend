package org.sopt.makers.operation.lecture.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
	SEMINAR("세미나"), EVENT("행사"), ETC("기타");

	private final String name;
}
