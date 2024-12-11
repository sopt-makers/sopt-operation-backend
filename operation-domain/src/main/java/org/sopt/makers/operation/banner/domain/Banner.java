package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.makers.operation.common.domain.BaseEntity;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Getter
@Entity
@Table(name = "banners")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PublishLocation location;

    @Column(name = "content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    private String publisher;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
    })
    private PublishPeriod period;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "pcImageUrl", column = @Column(name = "img_url_pc", nullable = false)),
            @AttributeOverride(name = "mobileImageUrl", column = @Column(name = "img_url_mobile", nullable = false))
    })
    private BannerImage image;

    @Builder
    private Banner(PublishLocation location, ContentType contentType, String publisher, PublishPeriod period, BannerImage image) {
        this.location = location;
        this.contentType = contentType;
        this.publisher = publisher;
        this.period = period;
        this.image = image;
    }

    public void updateLocation(PublishLocation location) {
        this.location = location;
    }

    public void updateContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void updatePublisher(String publisher) {
        this.publisher = publisher;
    }
}
