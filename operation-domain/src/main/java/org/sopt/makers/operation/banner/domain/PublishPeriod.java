package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.Embeddable;

import lombok.*;

import java.time.LocalDate;
import org.sopt.makers.operation.exception.*;

import static lombok.AccessLevel.PROTECTED;
import static org.sopt.makers.operation.code.failure.BannerFailureCode.INVALID_BANNER_PERIOD;

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

    @Builder
    private PublishPeriod(LocalDate startDate, LocalDate endDate) {
        validatePublishPeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validatePublishPeriod(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BannerException(INVALID_BANNER_PERIOD);
        }
    }

}
