package org.sopt.makers.operation.common.exception;

public class SubLectureException extends RuntimeException {
    public SubLectureException(String message) {
        super("[SubLectureException] : " + message);
    }
}
