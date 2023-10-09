package org.sopt.makers.operation.util;

import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Generation32 {

	public static float getUpdateScore(Attribute attribute, AttendanceStatus status) {
		return switch (attribute) {
			case SEMINAR -> getUpdateScoreInSeminar(status);
			case EVENT -> getUpdateScoreInEvent(status);
			default -> 0;
		};
	}

	private static float getUpdateScoreInSeminar(AttendanceStatus status) {
		return switch (status) {
			case TARDY -> -0.5f;
			case ABSENT -> -1f;
			default -> 0f;
		};
	}

	private static float getUpdateScoreInEvent(AttendanceStatus status) {
		if (status.equals(ATTENDANCE)) {
			return 0.5f;
		}
		return 0;
	}
}
