package org.example.shallweeatbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class VoteExceptionHandler {

    @ExceptionHandler(VoteLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleVoteLimitExceededException(VoteLimitExceededException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Vote Limit Exceeded");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateVoteException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVoteException(DuplicateVoteException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Duplicate Vote");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
