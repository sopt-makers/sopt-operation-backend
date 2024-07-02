package org.sopt.makers.operation.alarm.domain;

import static java.util.Objects.nonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.makers.operation.common.domain.BaseEntity;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.schedule.converter.StringListConverter;

@Entity
@NoArgsConstructor
@Getter
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Enumerated(value = EnumType.STRING)
    private Part part;

    @Enumerated(value = EnumType.STRING)
    private LinkType linkType;

    private String link;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AlarmType alarmType;

    @Enumerated(value = EnumType.STRING)
    private TargetType targetType;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> targetList;

    private String sendAt;

    private Integer createdGeneration;

    @Builder
    public Alarm(
            Category category,
            String title,
            String content,
            AlarmType alarmType,
            String link,
            LinkType linkType,
            Part part,
            Status status,
            TargetType targetType,
            List<String> targetList,
            Integer createdGeneration
    ) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.alarmType = alarmType;
        this.linkType = linkType;
        this.targetType = targetType;
        setLink(link);
        setTargetsInfo(part, targetList);
        this.status = status;
        this.createdGeneration = createdGeneration;
    }

    private void setLink(String link) {
        if (nonNull(link)) {
            this.link = link;
        }
    }

    private void setTargetsInfo(Part part, List<String> targetList) {
        if (nonNull(targetList)) {
            this.targetList = targetList;
        } else {
            this.part = part;
            this.targetList = new ArrayList<>();
        }
    }

    public void updateToSent(String sendAt) {
        this.sendAt = sendAt;
        this.status = Status.COMPLETED;
    }

    public boolean hasTargets() {
        return Objects.nonNull(this.targetList) && this.targetList.size() > 0;
    }
}
