package org.sopt.makers.operation.entity.calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.makers.operation.entity.BaseEntity;
import org.sopt.makers.operation.entity.alarm.Attribute;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
public class Calendar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    private LocalDateTime date;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private Attribute attribute;

    private String title;

    private String location;
}
