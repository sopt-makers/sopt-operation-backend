package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final SubAttendanceRepository subAttendanceRepository;

	@Override
	@Transactional
	public AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO) {
		SubAttendance subAttendance = subAttendanceRepository.findById(requestDTO.subAttendanceId())
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ATTENDANCE.getName()));
		subAttendance.updateStatus(requestDTO.status());

		Attendance attendance = subAttendance.getAttendance();
		SubLecture subLecture = subAttendance.getSubLecture();
		Member member = attendance.getMember();

		if (attendance.getSubAttendances().size() == subLecture.getRound()) {
			switch (requestDTO.attribute()) {
				case SEMINAR -> {
					return updateMemberScoreInSeminar(subAttendance);
				}
				case EVENT -> {
					return updateMemberScoreInEvent(subAttendance);
				}
			}
		}

		return new AttendanceResponseDTO(member.getId(), ABSENT, 0, member.getScore());
	}

	private AttendanceResponseDTO updateMemberScoreInSeminar(SubAttendance subAttendance) {
		AttendanceStatus status = getStatusIn32Seminar(subAttendance.getAttendance().getSubAttendances());
		Member member = subAttendance.getAttendance().getMember();
		float score = getScoreIn32Seminar(status);
		member.updateScore(score);
		return new AttendanceResponseDTO(member.getId(), status, score, member.getScore());
	}

	private AttendanceResponseDTO updateMemberScoreInEvent(SubAttendance subAttendance) {
		AttendanceStatus status = getStatusIn32Event(subAttendance.getAttendance().getSubAttendances());
		Member member = subAttendance.getAttendance().getMember();
		float score = getScoreIn32Event(status);
		member.updateScore(score);
		return new AttendanceResponseDTO(member.getId(), status, score, member.getScore());
	}

	private float getScoreIn32Seminar(AttendanceStatus status) {
		switch (status) {
			case ATTENDANCE -> {
				return 0;
			}
			case TARDY -> {
				return -0.5f;
			}
			case ABSENT -> {
				return -1;
			}
		}
		return 0;
	}

	private float getScoreIn32Event(AttendanceStatus status) {
		if (status.equals(ATTENDANCE)) {
			return 0.5f;
		}
		return 0;
	}

	private AttendanceStatus getStatusIn32Seminar(List<SubAttendance> attendances) {
		if (attendances.get(0).getStatus().equals(ATTENDANCE) && attendances.get(1).getStatus().equals(ATTENDANCE)) {
			return ATTENDANCE; // 0
		} else if (attendances.get(0).getStatus().equals(ABSENT) && attendances.get(1).getStatus().equals(ATTENDANCE)) {
			return TARDY; // -0.5
		}
		return ABSENT; // -1
	}

	private AttendanceStatus getStatusIn32Event(List<SubAttendance> attendances) {
		return attendances.get(1).getStatus().equals(ATTENDANCE) ? ATTENDANCE : ABSENT;
	}
}