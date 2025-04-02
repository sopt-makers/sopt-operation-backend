package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.AttributeOverride;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.sopt.makers.operation.common.domain.BaseEntity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

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

    private String link;

    @Column(name = "pc_image_key")
    private String pcImageKey;

    @Column(name = "mobile_image_key")
    private String mobileImageKey;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
    })
    private PublishPeriod period;

    @Builder
    private Banner(PublishLocation location, String link, ContentType contentType, String publisher, PublishPeriod period, String pcImageKey, String mobileImageKey) {
        this.location = location;
        this.link = link;
        this.contentType = contentType;
        this.publisher = publisher;
        this.period = period;
        this.pcImageKey = pcImageKey;
        this.mobileImageKey = mobileImageKey;
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

    public void updateLink(String link) { this.link = link;}

    public void updatePeriod(PublishPeriod period) { this.period = period; }

    public void updatePcImageKey(String pcImageKey) {
        this.pcImageKey = pcImageKey;
    }

    public void updateMobileImageKey(String mobileImageKey) {
        this.mobileImageKey = mobileImageKey;
    }
}
