package ru.courses.exception;

public class LineLengthException extends RuntimeException {
    public LineLengthException() {
    }

    public LineLengthException(String message) {
        super(message);
    }
}
