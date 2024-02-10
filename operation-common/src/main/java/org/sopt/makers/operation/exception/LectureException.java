package org.sopt.makers.operation.exception;

import static org.sopt.makers.operation.code.failure.lecture.LectureFailureCode.*;

import org.sopt.makers.operation.code.failure.FailureCode;
import org.sopt.makers.operation.code.failure.lecture.LectureFailureCode;

import lombok.Getter;

@Getter
public class LectureException extends RuntimeException {

    private final FailureCode failureCode;

    public LectureException(FailureCode failureCode) {
        super("[LectureException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }

    public LectureException(FailureCode failureCode, int round) {
        super("[LectureException] : " + failureCode.getMessage());
        this.failureCode = getFailureCodeByRound(round);
    }

    private LectureFailureCode getFailureCodeByRound(int round) {
        return switch (round) {
            case 1 -> ENDED_FIRST_ATTENDANCE;
            case 2 -> ENDED_SECOND_ATTENDANCE;
            default-> ENDED_ATTENDANCE;
        };
    }
}