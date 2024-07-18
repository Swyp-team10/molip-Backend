package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String getUsername(CustomOAuth2User principal) {
        String providerId = principal.getProviderId();
        User user = userRepository.findByProviderId(providerId);
        if (user != null) {
            return user.getName();
        }
        return null;
    }
}
