package com.example.backend.exception;

public class NoJobsFoundException extends RuntimeException {
    public NoJobsFoundException(String message) {
        super(message);
    }
}
