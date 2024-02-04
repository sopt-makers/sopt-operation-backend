package org.sopt.makers.operation.domain.schedule.domain;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.sopt.makers.operation.domain.BaseEntity;
import org.sopt.makers.operation.domain.lecture.Attribute;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private Attribute attribute;

    private String title;

    private String location;
}
