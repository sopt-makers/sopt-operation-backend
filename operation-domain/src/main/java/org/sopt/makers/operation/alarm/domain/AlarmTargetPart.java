package org.sopt.makers.operation.alarm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.member.domain.Part;

import static lombok.AccessLevel.PRIVATE;
import static org.sopt.makers.operation.code.failure.AlarmFailureCode.INVALID_ALARM_TARGET_TYPE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AlarmTargetPart {
    ALL("전체"),
    PLAN("기획"),
    DESIGN("디자인"),
    WEB("웹"),
    ANDROID("안드로이드"),
    iOS("iOS"),
    SERVER("서버"),
    UNDEFINED("없음")
    ;

    private final String name;

    public Part toPartDomain() {
        return switch (this) {
            case ALL -> Part.ALL;
            case PLAN -> Part.PLAN;
            case DESIGN -> Part.DESIGN;
            case ANDROID -> Part.ANDROID;
            case iOS -> Part.IOS;
            case WEB -> Part.WEB;
            case SERVER -> Part.SERVER;
            default -> throw new AlarmException(INVALID_ALARM_TARGET_TYPE);
        };
    }
}
