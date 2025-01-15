package org.sopt.makers.operation.alarm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

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
}
