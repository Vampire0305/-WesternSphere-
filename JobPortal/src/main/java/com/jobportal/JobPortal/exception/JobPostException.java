package com.jobportal.JobPortal.exception;

public class JobPostException extends RuntimeException {
    public JobPostException(String message) {
        super(message);
    }

    public JobPostException(String message, Throwable cause) {
        super(message, cause);
    }
}
