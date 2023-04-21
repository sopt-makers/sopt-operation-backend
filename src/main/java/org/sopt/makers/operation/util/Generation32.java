package org.sopt.makers.operation.util;

import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import java.util.List;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.SubAttendance;
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

	public static AttendanceStatus getAttendanceStatus(Attribute attribute, List<SubAttendance> subAttendances) {
		int firstRound = subAttendances.get(0).getSubLecture().getRound() == 1 ? 0 : 1;
		SubAttendance first = subAttendances.get(firstRound);
		SubAttendance second = subAttendances.get(1 - firstRound);
		return switch (attribute) {
			case SEMINAR -> getAttendanceStatusInSeminar(first, second);
			case EVENT -> getAttendanceStatusInEvent(second);
			case ETC -> getAttendanceStatusInEtc(second);
		};
	}

	private static AttendanceStatus getAttendanceStatusInSeminar(SubAttendance first, SubAttendance second) {
		if (first.getStatus().equals(ATTENDANCE) && second.getStatus().equals(ATTENDANCE)) {
			return ATTENDANCE;
		} else if (first.getStatus().equals(ABSENT) && second.getStatus().equals(ATTENDANCE)) {
			return TARDY;
		}
		return ABSENT;
	}

	private static AttendanceStatus getAttendanceStatusInEvent(SubAttendance second) {
		if (second.getStatus().equals(ATTENDANCE)) {
			return ATTENDANCE;
		}
		return ABSENT;
	}

	private static AttendanceStatus getAttendanceStatusInEtc(SubAttendance second) {
		if (second.getStatus().equals(ATTENDANCE)) {
			return PARTICIPATE;
		}
		return NOT_PARTICIPATE;
	}
}
