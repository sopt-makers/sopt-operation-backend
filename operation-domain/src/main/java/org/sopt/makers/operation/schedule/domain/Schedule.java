package org.sopt.makers.operation.schedule.domain;

import java.time.LocalDateTime;

import org.sopt.makers.operation.common.domain.BaseEntity;
import org.sopt.makers.operation.lecture.domain.Attribute;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Attribute attribute;

    private String title;

    private String location;
}
