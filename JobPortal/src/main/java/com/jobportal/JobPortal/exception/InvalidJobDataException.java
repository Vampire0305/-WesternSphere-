package com.jobportal.JobPortal.exception;

public class InvalidJobDataException extends RuntimeException {
    public InvalidJobDataException(String message) {
        super(message);
    }

    public InvalidJobDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
