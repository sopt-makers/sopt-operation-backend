package org.sopt.makers.operation.web.member.service;

import java.util.EnumMap;
import java.util.List;

import org.operation.attendance.domain.AttendanceStatus;
import org.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.dto.MemberSearchCondition;
import org.operation.member.domain.Member;
import org.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.member.dto.response.AttendanceTotalCountVO;
import org.sopt.makers.operation.web.member.dto.response.AttendanceTotalVO;
import org.sopt.makers.operation.web.member.dto.response.MemberListGetResponse;
import org.sopt.makers.operation.web.member.dto.response.MembersResponse;
import org.springframework.data.domain.Pageable;
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

    @Override
    public MembersResponse getMemberList(Part part, int generation, Pageable pageable) {
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

        return MembersResponse.of(members, membersCount);
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
