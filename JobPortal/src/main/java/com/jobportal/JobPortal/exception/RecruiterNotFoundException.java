package com.jobportal.JobPortal.exception;

public class RecruiterNotFoundException extends RuntimeException {
    public RecruiterNotFoundException(String message) {
        super(message);
    }

    public RecruiterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
