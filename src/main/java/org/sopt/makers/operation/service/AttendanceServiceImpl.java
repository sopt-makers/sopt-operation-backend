package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.AttendanceStatus.*;
import static org.sopt.makers.operation.entity.lecture.Attribute.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

<<<<<<< HEAD
import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
=======
import lombok.val;
import org.sopt.makers.operation.dto.attendance.*;
>>>>>>> develop
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Attribute;
<<<<<<< HEAD
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
=======
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
>>>>>>> develop
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.sopt.makers.operation.util.Generation32;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
<<<<<<< HEAD
	private final LectureRepository lectureRepository;
=======
	private final SubLectureRepository subLectureRepository;
>>>>>>> develop
	private final AttendanceRepository attendanceRepository;
	private final Generation32 sopt32;

	@Override
	@Transactional
	public AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO) {
		SubAttendance subAttendance = findSubAttendance(requestDTO.subAttendanceId());
		subAttendance.updateStatus(requestDTO.status());
		Attendance attendance = subAttendance.getAttendance();
		attendance.updateStatus(sopt32.getAttendanceStatus(requestDTO.attribute(), attendance.getSubAttendances()));
		return AttendanceResponseDTO.of(subAttendance);
	}

	@Override
	public AttendanceMemberResponseDTO getMemberAttendance(Long memberId) {
		Member member = findMember(memberId);
		return AttendanceMemberResponseDTO.of(member);
	}

	@Override
	@Transactional
	public float updateMemberScore(Long memberId) {
		Member member = findMember(memberId);
		float score = (float)(2 + member.getAttendances().stream()
			.mapToDouble(attendance -> getUpdateScore(attendance, attendance.getLecture().getAttribute()))
			.sum());
		member.setScore(score);
		return member.getScore();
	}

	@Override
<<<<<<< HEAD
	public List<MemberResponseDTO> getMemberAttendances(Long lectureId, Part part, Pageable pageable) {
		Lecture lecture = findLecture(lectureId);
		List<Attendance> attendances = attendanceRepository.findLectureAttendances(lecture, part, pageable);
		return attendances.stream().map(attendance ->
			MemberResponseDTO.of(
				attendance,
				sopt32.getUpdateScore(lecture.getAttribute(), attendance.getStatus())
			)
		).toList();
=======
	@Transactional
	public AttendResponseDTO attend(Long memberId, AttendRequestDTO requestDTO) {
		val now = LocalDateTime.now();

		val subLecture = subLectureRepository.findById(requestDTO.subLectureId())
				.orElseThrow(() -> new EntityNotFoundException(INVALID_SUB_LECTURE.getName()));

		if(!subLecture.getCode().equals(requestDTO.code())) throw new SubLectureException(INVALID_CODE.getName());

		if(now.isBefore(subLecture.getStartAt())) throw new LectureException(subLecture.getRound() + NOT_STARTED_NTH_ATTENDANCE.getName());

		if(now.isAfter(subLecture.getStartAt().plusMinutes(10))) throw new LectureException(ENDED_ATTENDANCE.getName());

		Attendance attendance = attendanceRepository.findAttendanceByLectureIdAndMemberId(subLecture.getLecture().getId(), memberId);

		val subAttendance = attendance.getSubAttendances().stream()
				.filter(subAttendance3 ->
						(subAttendance3.getSubLecture().getId().equals(requestDTO.subLectureId())
								&& subAttendance3.getAttendance().getId().equals(attendance.getId()))
				).toList();

		subAttendance.get(0).updateStatus(AttendanceStatus.ATTENDANCE);

		if(subLecture.getRound() == 2) {
			attendance.updateStatus(sopt32.getAttendanceStatus(attendance.getLecture().getAttribute(), attendance.getSubAttendances()));
			this.updateMemberScore(memberId);
		}

		return AttendResponseDTO.of(subLecture.getId());
>>>>>>> develop
	}

	private float getUpdateScore(Attendance attendance, Attribute attribute) {
		if (attribute.equals(SEMINAR)) {
			if (attendance.getStatus().equals(TARDY)) {
				return -0.5f;
			} else if (attendance.getStatus().equals(ABSENT)) {
				return -1;
			}
		} else if (attribute.equals(EVENT)) {
			if (attendance.getStatus().equals(ATTENDANCE)) {
				return 0.5f;
			}
		}
		return 0;
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_MEMBER.getName()));
	}

	private SubAttendance findSubAttendance(Long id) {
		return subAttendanceRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ATTENDANCE.getName()));
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
			.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));
	}
}
