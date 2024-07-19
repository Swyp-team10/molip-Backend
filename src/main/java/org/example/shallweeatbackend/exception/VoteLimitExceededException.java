package org.example.shallweeatbackend.exception;

public class VoteLimitExceededException extends RuntimeException {
    public VoteLimitExceededException(String message) {
        super(message);
    }
}
