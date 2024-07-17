package org.example.shallweeatbackend.exception;

public class PersonalBoardNotFoundException extends RuntimeException {
    public PersonalBoardNotFoundException(String message) {
        super(message);
    }
}