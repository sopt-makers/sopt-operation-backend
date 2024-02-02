package org.operation.schedule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.operation.common.domain.BaseEntity;
import org.operation.lecture.Attribute;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

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
