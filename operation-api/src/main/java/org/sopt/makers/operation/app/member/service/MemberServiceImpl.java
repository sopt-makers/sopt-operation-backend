package org.sopt.makers.operation.app.member.service;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;
import static org.sopt.makers.operation.code.failure.member.memberFailureCode.*;
import static org.sopt.makers.operation.lecture.domain.Attribute.*;

import java.util.List;

import org.sopt.makers.operation.app.member.dto.response.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.app.attendance.dto.response.AttendanceTotalVO;
import org.sopt.makers.operation.app.member.dto.response.MemberScoreGetResponse;
import org.sopt.makers.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;
    private final ValueConfig valueConfig;

    @Override
    public AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId) {
        val member = memberRepository.getMemberByPlaygroundIdAndGeneration(playGroundId, valueConfig.getGENERATION())
                .orElseThrow(() -> new MemberException(INVALID_MEMBER));

        val attendances = findAttendances(member);

        val filteredAttendances = filterEtcNoAppearance(attendances);

        return AttendanceTotalResponseDTO.of(member, filteredAttendances);
    }

    @Override
    public MemberScoreGetResponse getMemberScore(Long playGroundId) {
        val member = memberRepository.getMemberByPlaygroundIdAndGeneration(playGroundId, valueConfig.getGENERATION())
                .orElseThrow(() -> new MemberException(INVALID_MEMBER));

        return MemberScoreGetResponse.of(member.getScore());
    }

    private List<AttendanceTotalVO> filterEtcNoAppearance(List<AttendanceTotalVO> attendances) {
        return attendances.stream()
            .filter(attendanceTotalVO ->
                !(attendanceTotalVO.attribute().equals(ETC)
                && attendanceTotalVO.status().equals(NOT_PARTICIPATE))
            )
            .toList();
    }

    private List<AttendanceTotalVO> findAttendances(Member member) {
        return attendanceRepository.findAttendanceByMemberId(member.getId())
                .stream().map(AttendanceTotalVO::getTotalAttendanceVO)
                .toList();
    }
}
