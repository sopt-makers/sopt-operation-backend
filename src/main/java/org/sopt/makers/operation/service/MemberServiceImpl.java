package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalCountVO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalVO;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.dto.member.MemberRequestDTO;
import org.sopt.makers.operation.dto.member.MemberScoreGetResponse;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public List<MemberListGetResponse> getMemberList(Part part, int generation) {
        if(part.equals(Part.ALL)) {
            part = null;
        }

        val members = memberRepository.search(new MemberSearchCondition(part, generation));

        return members.stream().map(member -> {
            val attendances = findAttendances(member);
            val countAttendance = countAttendance(attendances);
            val total = translateAttendanceStatus(countAttendance);
            return MemberListGetResponse.of(member, total);
        }).toList();
    }

    @Override
    public AttendanceTotalResponseDTO getMemberTotalAttendance(Long playGroundId) {
        val member = memberRepository.getMemberByPlaygroundId(playGroundId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        val attendances = findAttendances(member);
        val countAttendance = countAttendance(attendances);
        val total = translateAttendanceStatus(countAttendance);

        val filteredAttendances = filterEtcNoAppearance(attendances);

        return AttendanceTotalResponseDTO.of(member, total, filteredAttendances);
    }

    @Override
    public MemberScoreGetResponse getMemberScore(Long playGroundId) {
        val member = memberRepository.getMemberByPlaygroundId(playGroundId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        return MemberScoreGetResponse.of(member.getScore());
    }

    @Override
    @Transactional
    public void createMember(Long playGroundId, MemberRequestDTO requestDTO) {
        if (memberRepository.existsByPlaygroundId(playGroundId)) {
            throw new IllegalStateException(DUPLICATED_MEMBER.getName());
        }
        memberRepository.save(new Member(playGroundId, requestDTO));
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
