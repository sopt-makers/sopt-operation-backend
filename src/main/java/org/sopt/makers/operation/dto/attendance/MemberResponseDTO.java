package org.sopt.makers.operation.dto.attendance;

import static org.sopt.makers.operation.util.Generation32.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.sopt.makers.operation.entity.AttendanceStatus;

public record MemberResponseDTO (
	Long attendanceId,
	MemberVO member,
	List<SubAttendanceVO> attendances,
	float updatedScore) {

	public static MemberResponseDTO of(ArrayList<LectureInfo> infos) {
		LectureInfo info = infos.get(0);
		return new MemberResponseDTO(
			info.attendanceId(),
			MemberVO.of(info),
			infos.stream().map(SubAttendanceVO::of).toList(),
			getUpdateScore(info.attribute(), info.attendanceStatus())
		);
	}
}

record MemberVO(Long memberId, String name, String university) {
	static MemberVO of(LectureInfo info) {
		return new MemberVO(
			info.memberId(),
			info.memberName(),
			info.university()
		);
	}
}

record SubAttendanceVO(Long subAttendanceId, int round, AttendanceStatus status, String updateAt) {
	static SubAttendanceVO of(LectureInfo info) {
		return new SubAttendanceVO(
			info.subAttendanceId(),
			info.round(),
			info.subAttendanceStatus(),
			transfer(info.updatedAt())
		);
	}

	private static String transfer(LocalDateTime time) {
		return time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	}
}
