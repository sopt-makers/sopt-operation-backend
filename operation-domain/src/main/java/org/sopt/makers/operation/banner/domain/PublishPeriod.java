package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
        boolean isReserved = date.isBefore(startDate);
        boolean isDone = date.isAfter(endDate);
        if (isDone) {
            return PublishStatus.DONE;
        } else if (isReserved){
            return PublishStatus.RESERVED;
        }
        return PublishStatus.IN_PROGRESS;
    }

}
