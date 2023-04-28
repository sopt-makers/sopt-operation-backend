package org.sopt.makers.operation.dto.attendance;

import static org.sopt.makers.operation.util.Generation32.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sopt.makers.operation.entity.Member;

import lombok.val;

public record AttendanceMemberResponseDTO(
	String name,
	float score,
	String part,
	String university,
	String phone,
	List<LectureVO> lectures
) {
	public static AttendanceMemberResponseDTO of(Member member, HashMap<Long, ArrayList<MemberInfo>> map) {
		return new AttendanceMemberResponseDTO(
			member.getName(),
			member.getScore(),
			member.getPart().getName(),
			member.getUniversity(),
			member.getPhone(),
			map.keySet().stream().map(key -> LectureVO.of(map.get(key))).toList()
		);
	}
}

record LectureVO(
	String lecture,
	float additiveScore,
	String status,
	List<AttendanceVO> attendances
) {
	public static LectureVO of(ArrayList<MemberInfo> infos) {
		val info = infos.get(0);
		return new LectureVO(
			info.lectureName(),
			getUpdateScore(info.lectureAttribute(), info.attendanceStatus()),
			infos.get(0).attendanceStatus().getName(),
			infos.stream().map(AttendanceVO::of).toList()
		);
	}
}

record AttendanceVO(
	int round,
	String status,
	String date
) {
	public static AttendanceVO of(MemberInfo info) {
		return new AttendanceVO(
			info.round(),
			info.subAttendanceStatus().getName(),
			convertDate(info.updatedAt())
		);
	}

	private static String convertDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	}
}
