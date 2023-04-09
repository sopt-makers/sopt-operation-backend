package org.sopt.makers.operation.service;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalCountVO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalVO;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public Optional<Member> confirmMember(Long playGroundId) {
        return Optional.ofNullable(memberRepository.getMemberByPlaygroundId(playGroundId));
    }

    @Override
    public List<MemberListGetResponse> getMemberList(Part part, int generation) {
        if(part.equals(Part.ALL)) {
            part = null;
        }

        val members = memberRepository.search(new MemberSearchCondition(part, generation));

        val memberList = members.stream().map(member -> {
            val attendances = attendanceRepository.findAttendanceByMemberId(member.getId())
                    .stream().map(AttendanceTotalVO::getTotalAttendanceVO)
                    .toList();

            val countAttendance = attendances.stream()
                    .map(AttendanceTotalVO::getAttendanceStatus)
                    .collect(
                            () -> new EnumMap<>(AttendanceStatus.class),
                            (map, status) -> map.merge(status, 1, (count, increment) -> (int)count + (int)increment),
                            (map1, map2) -> map2.forEach((status, count) -> map1.merge(status, count, (c1, c2) -> (int)c1 + (int)c2))
                    );

            val total = AttendanceTotalCountVO.of(
                    (int) countAttendance.getOrDefault(AttendanceStatus.ATTENDANCE, 0),
                    (int) countAttendance.getOrDefault(AttendanceStatus.ABSENT, 0),
                    (int) countAttendance.getOrDefault(AttendanceStatus.TARDY, 0),
                    (int) countAttendance.getOrDefault(AttendanceStatus.PARTICIPATE, 0)
            );

            return MemberListGetResponse.of(member, total);
        }).collect(Collectors.toList());

        return memberList;
    }
}
