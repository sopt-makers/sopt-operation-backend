package org.sopt.makers.operation.app.member.service;

import static org.operation.member.message.ErrorMessage.*;

import java.util.EnumMap;
import java.util.List;

import org.sopt.makers.operation.app.member.dto.response.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.app.member.dto.response.AttendanceTotalVO;
import org.sopt.makers.operation.app.member.dto.response.MemberScoreGetResponse;
import org.operation.attendance.domain.AttendanceStatus;
import org.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.common.config.ValueConfig;
import org.sopt.makers.operation.common.exception.MemberException;
import org.operation.lecture.Attribute;
import org.operation.member.domain.Member;
import org.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.member.dto.response.AttendanceTotalCountVO;
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
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getContent()));

        val attendances = findAttendances(member);
        val countAttendance = countAttendance(attendances);
        val total = translateAttendanceStatus(countAttendance);

        val filteredAttendances = filterEtcNoAppearance(attendances);

        return AttendanceTotalResponseDTO.of(member, total, filteredAttendances);
    }

    @Override
    public MemberScoreGetResponse getMemberScore(Long playGroundId) {
        val member = memberRepository.getMemberByPlaygroundIdAndGeneration(playGroundId, valueConfig.getGENERATION())
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getContent()));

        return MemberScoreGetResponse.of(member.getScore());
    }

    private List<AttendanceTotalVO> filterEtcNoAppearance(List<AttendanceTotalVO> attendances) {
        return attendances.stream()
            .filter(attendanceTotalVO ->
                !(attendanceTotalVO.attribute().equals(Attribute.ETC)
                && attendanceTotalVO.status().equals(AttendanceStatus.NOT_PARTICIPATE))
            )
            .toList();
    }

    private List<AttendanceTotalVO> findAttendances(Member member) {
        return attendanceRepository.findAttendanceByMemberId(member.getId())
                .stream().map(AttendanceTotalVO::getTotalAttendanceVO)
                .toList();
    }

    private EnumMap<AttendanceStatus, Integer> countAttendance(List<AttendanceTotalVO> attendances) {
        return attendances.stream()
            .map(AttendanceTotalVO::getAttendanceStatus)
            .collect(
                () -> new EnumMap<>(AttendanceStatus.class),
                (map, status) -> map.merge(status, 1, Integer::sum),
                (map1, map2) -> map2.forEach((status, count) -> map1.merge(status, count, Integer::sum))
            );
    }

    private AttendanceTotalCountVO translateAttendanceStatus(EnumMap<AttendanceStatus, Integer> countAttendance) {
        return AttendanceTotalCountVO.of(
            countAttendance.getOrDefault(AttendanceStatus.ATTENDANCE, 0),
            countAttendance.getOrDefault(AttendanceStatus.ABSENT, 0),
            countAttendance.getOrDefault(AttendanceStatus.TARDY, 0),
            countAttendance.getOrDefault(AttendanceStatus.PARTICIPATE, 0)
        );
    }
}
