package org.sopt.makers.operation.service;

import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalCountVO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalVO;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import org.sopt.makers.operation.service.AttendanceServiceImpl;

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
        List<Member> members = memberRepository.getMembersByPartAndGeneration(part, generation);

        List<MemberListGetResponse> memberList = members.stream().map(member -> {
            List<AttendanceTotalVO> attendances = attendanceRepository.findAttendanceByMemberId(member.getId())
                    .stream().map(AttendanceTotalVO::getTotalAttendanceVO)
                    .toList();

            Map<AttendanceStatus, Integer> countAttendance = attendances.stream()
                    .map(AttendanceTotalVO::getAttendanceStatus)
                    .collect(
                            () -> new EnumMap<>(AttendanceStatus.class),
                            (map, status) -> map.merge(status, 1, Integer::sum),
                            (map1, map2) -> map2.forEach((status, count) -> map1.merge(status, count, Integer::sum))
                    );

            AttendanceTotalCountVO total = AttendanceTotalCountVO.of(
                    countAttendance.size(),
                    countAttendance.getOrDefault(AttendanceStatus.ATTENDANCE, 0),
                    countAttendance.getOrDefault(AttendanceStatus.ABSENT, 0),
                    countAttendance.getOrDefault(AttendanceStatus.TARDY, 0)
            );

            return MemberListGetResponse.of(member, total);
        }).collect(Collectors.toList());

        return memberList;
    }
}
