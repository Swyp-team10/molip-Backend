package org.example.shallweeatbackend.controller;

import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.service.SearchService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchWordService;

    @PostMapping("/map/search")
    public void saveSearchWord(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam String word) {
        if (principal == null) {
            throw new IllegalStateException("사용자가 인증되지 않았습니다.");
        }

        searchWordService.saveWord(principal.getProviderId(), word);
    }

}
