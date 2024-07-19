package org.example.shallweeatbackend.exception;

public class UnauthorizedVoteException extends RuntimeException {
    public UnauthorizedVoteException(String message) {
        super(message);
    }
}
