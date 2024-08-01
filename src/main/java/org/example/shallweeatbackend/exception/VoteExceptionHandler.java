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

    @ExceptionHandler(UnauthorizedVoteException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedVoteException(UnauthorizedVoteException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Unauthorized Vote");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // 모든 RuntimeException을 처리하는 핸들러 (위의 특정 예외보다 범위가 넓음)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Runtime Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 일반적인 Exception을 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
