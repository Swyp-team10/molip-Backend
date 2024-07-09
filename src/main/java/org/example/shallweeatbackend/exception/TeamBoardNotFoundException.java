package org.example.shallweeatbackend.exception;

public class TeamBoardNotFoundException extends RuntimeException{
    public TeamBoardNotFoundException(String message) {
        super(message);
    }
}
