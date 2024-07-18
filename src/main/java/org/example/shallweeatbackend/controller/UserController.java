package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/username")
    public ResponseEntity<Map<String, String>> getUsername(@AuthenticationPrincipal CustomOAuth2User principal) {
        Map<String, String> response = new HashMap<>();
        String username = userService.getUsername(principal);
        response.put("username", username);
        return ResponseEntity.ok(response);
    }
}