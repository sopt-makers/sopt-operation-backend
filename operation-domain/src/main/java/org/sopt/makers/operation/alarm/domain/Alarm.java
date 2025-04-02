package org.sopt.makers.operation.alarm.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.sopt.makers.operation.common.domain.BaseEntity;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "alarms")
@Builder(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmStatus status;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    @Embedded
    private AlarmTarget target;

    @Embedded
    private AlarmContent content;

    private LocalDateTime intendedAt;

    private LocalDateTime sendAt;

    public static Alarm instant(AlarmTarget target, AlarmContent content) {
        return Alarm.builder()
                .status(AlarmStatus.COMPLETED)
                .type(AlarmType.INSTANT)
                .target(target)
                .content(content)
                .intendedAt(LocalDateTime.now())
                .build();
    }

    public static Alarm scheduled(AlarmTarget target, AlarmContent content, LocalDateTime intendedAt) {
        return Alarm.builder()
                .status(AlarmStatus.SCHEDULED)
                .type(AlarmType.RESERVED)
                .target(target)
                .content(content)
                .intendedAt(intendedAt)
                .build();
    }

    public void updateStatusToComplete(LocalDateTime successAt) {
        this.status = AlarmStatus.COMPLETED;
        this.sendAt = successAt;
    }

}
