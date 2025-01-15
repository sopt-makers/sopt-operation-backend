package org.sopt.makers.operation.alarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.code.failure.AlarmFailureCode;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.sopt.makers.operation.constant.AlarmConstant.isSupportedAppLink;

@Getter
@Builder(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Embeddable
public class AlarmContent {

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    AlarmCategory category;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "content", columnDefinition = "TEXT")
    String content;

    @Column(name = "link_path", columnDefinition = "TEXT")
    String linkPath;

    @Column(name = "link_type", nullable = false)
    @Enumerated(EnumType.STRING)
    AlarmLinkType linkType;

    public static AlarmContent withAppLink(String title, String content, AlarmCategory category, String link) {
        if (!isSupportedAppLink(link)) {
            throw new AlarmException(AlarmFailureCode.INVALID_LINK);
        }
        return AlarmContent.builder()
                .title(title)
                .content(content)
                .linkPath(link)
                .linkType(AlarmLinkType.APP)
                .build();
    }

    public static AlarmContent withWebLink(String title, String content, AlarmCategory category, String link) {
        return AlarmContent.builder()
                .title(title)
                .content(content)
                .linkPath(link)
                .linkType(AlarmLinkType.WEB)
                .build();
    }

    public static AlarmContent withoutLink(String title, String content, AlarmCategory category) {
        return AlarmContent.builder()
                .title(title)
                .content(content)
                .linkType(AlarmLinkType.NONE)
                .build();
    }

}
