package org.sopt.makers.operation.common.exception;

public class LectureException extends RuntimeException {
    public LectureException(String message) {
        super("[LectureException] : " + message);
    }
}