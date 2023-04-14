package org.sopt.makers.operation.exception;

public class SubLectureException extends RuntimeException {
    public SubLectureException(String message) {
        super("[SubLectureException] : " + message);
    }
}
