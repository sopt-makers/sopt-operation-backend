package org.sopt.makers.operation.alarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.sopt.makers.operation.schedule.converter.StringListConverter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Embeddable
public class AlarmTarget {

    @Column(name = "action", nullable = false, updatable = false, insertable = false)
    private AlarmSendAction sendAction;

    @Column(name = "part", nullable = false, updatable = false, insertable = false)
    private AlarmTargetPart targetPart;

    @Column(name = "target_type", nullable = false, updatable = false, insertable = false)
    private AlarmTargetType targetType;

    @Column(name = "generation", updatable = false, insertable = false)
    private Integer generation;

    @Column(name = "targets", columnDefinition = "TEXT", updatable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> targetIds;

    public static AlarmTarget all() {
        return AlarmTarget.builder()
                .sendAction(AlarmSendAction.SEND_ALL)
                .targetType(AlarmTargetType.ALL)
                .targetPart(AlarmTargetPart.ALL)
                .build();
    }

    public static AlarmTarget partialForAll(AlarmTargetPart targetPart, List<String> targetIds) {
        return AlarmTarget.builder()
                .sendAction(AlarmSendAction.SEND)
                .targetType(AlarmTargetType.ALL)
                .targetPart(targetPart)
                .targetIds(targetIds)
                .build();
    }

    public static AlarmTarget partialForActive(int generation, AlarmTargetPart targetPart, List<String> targetIds) {
        return AlarmTarget.builder()
                .sendAction(AlarmSendAction.SEND)
                .targetType(AlarmTargetType.ACTIVE)
                .generation(generation)
                .targetPart(targetPart)
                .targetIds(targetIds)
                .build();
    }


    public static AlarmTarget partialForCsv(List<String> targetIds) {
        return AlarmTarget.builder()
                .sendAction(AlarmSendAction.SEND)
                .targetType(AlarmTargetType.CSV)
                .targetPart(AlarmTargetPart.UNDEFINED)
                .targetIds(targetIds)
                .build();
    }

}
