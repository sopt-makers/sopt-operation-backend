package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PublishPeriod {
    private LocalDate startDate;
    private LocalDate endDate;

    public void updateStartDate(LocalDate updateDate) {
        this.startDate = updateDate;
    }

    public void updateEndDate(LocalDate updateDate) {
        this.endDate = updateDate;
    }

    public PublishStatus getPublishStatus(LocalDate date) {
        if (date.isAfter(endDate)) {
            return PublishStatus.DONE;
        } else {
            if (date.isAfter(startDate)) {
                return PublishStatus.IN_PROGRESS;
            }
            return PublishStatus.RESERVED;
        }
    }

}
