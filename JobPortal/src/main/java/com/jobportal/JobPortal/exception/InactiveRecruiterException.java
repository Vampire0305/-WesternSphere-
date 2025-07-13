package com.jobportal.JobPortal.exception;

public class InactiveRecruiterException extends RuntimeException {
    public InactiveRecruiterException(String message) {
        super(message);
    }

    public InactiveRecruiterException(String message, Throwable cause) {
        super(message, cause);
    }
}
