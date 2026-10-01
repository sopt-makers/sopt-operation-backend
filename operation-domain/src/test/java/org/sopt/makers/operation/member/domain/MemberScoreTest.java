package org.sopt.makers.operation.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.ATTENDANCE;
import static org.sopt.makers.operation.lecture.domain.Attribute.SEMINAR;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;

class MemberScoreTest {

	@Test
	void 세션을_종료하면_기존_점수에_누적하지_않고_기본_점수부터_재계산한다() {
		Member member = createMember(0f);
		Lecture lecture = createSeminar();
		createAbsentAttendance(member, lecture);

		lecture.updateToEnd();

		assertThat(member.getScore()).isEqualTo(1f);
	}

	@Test
	void 종료된_세션의_출석을_정정하면_회원_점수를_즉시_재계산한다() {
		Member member = createMember(0f);
		Lecture lecture = createSeminar();
		SubAttendance[] subAttendances = createAbsentAttendance(member, lecture);
		lecture.updateToEnd();

		subAttendances[0].updateStatus(ATTENDANCE);

		assertThat(member.getScore()).isEqualTo(1.5f);

		subAttendances[1].updateStatus(ATTENDANCE);

		assertThat(member.getScore()).isEqualTo(2f);
	}

	@Test
	void 종료된_세션을_삭제하면_현재_출석_상태를_역산하지_않고_해당_세션을_제외해_재계산한다() {
		Member member = createMember(-5f);
		Lecture lecture = createSeminar();
		SubAttendance[] subAttendances = createAbsentAttendance(member, lecture);
		lecture.updateToEnd();
		subAttendances[0].updateStatus(ATTENDANCE);

		Attendance attendance = member.getAttendances().get(0);
		attendance.restoreMemberScore();

		assertThat(member.getScore()).isEqualTo(2f);
	}

	private Member createMember(float score) {
		return new Member(1L, "테스트", 39, Part.SERVER, score, new ArrayList<>());
	}

	private Lecture createSeminar() {
		return Lecture.builder()
				.name("테스트 세미나")
				.part(Part.SERVER)
				.generation(39)
				.place("온라인")
				.startDate(LocalDateTime.now().minusHours(2))
				.endDate(LocalDateTime.now().minusHours(1))
				.attribute(SEMINAR)
				.build();
	}

	private SubAttendance[] createAbsentAttendance(Member member, Lecture lecture) {
		Attendance attendance = new Attendance(member, lecture);
		SubLecture first = new SubLecture(lecture, 1);
		SubLecture second = new SubLecture(lecture, 2);
		return new SubAttendance[] {
				new SubAttendance(attendance, first),
				new SubAttendance(attendance, second)
		};
	}
}
