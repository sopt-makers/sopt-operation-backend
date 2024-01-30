package org.sopt.makers.operation.service.web.member;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.attendance.response.AttendanceTotalCountVO;
import org.sopt.makers.operation.dto.attendance.response.AttendanceTotalVO;
import org.sopt.makers.operation.dto.member.response.MemberListGetResponse;
import org.sopt.makers.operation.dto.member.response.MemberSearchCondition;
import org.sopt.makers.operation.dto.member.response.MembersResponseDTO;
import org.sopt.makers.operation.entity.attendance.AttendanceStatus;
import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public MembersResponseDTO getMemberList(Part part, int generation, Pageable pageable) {
        if (part.equals(Part.ALL)) {
            part = null;
        }

        val members = memberRepository.search(new MemberSearchCondition(part, generation), pageable)
            .stream().map(member -> {
                val attendances = findAttendances(member);
                val countAttendance = countAttendance(attendances);
                val total = translateAttendanceStatus(countAttendance);
                return MemberListGetResponse.of(member, total);
            }).toList();
        val membersCount = memberRepository.countByGenerationAndPart(generation, part);

        return MembersResponseDTO.of(members, membersCount);
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