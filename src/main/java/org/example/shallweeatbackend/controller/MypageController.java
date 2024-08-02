package org.example.shallweeatbackend.controller;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.UserDTO;
import org.example.shallweeatbackend.service.MypageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    // 사용자 정보 조회
    @GetMapping("/info")
    public UserDTO getUserInfo(@AuthenticationPrincipal CustomOAuth2User principal) {
        return mypageService.getUserInfo(principal.getProviderId());
    }

    // 전체 팀 메뉴판에서의 투표 결과 조회
    @GetMapping("/votes")
    public List<Map<String, Object>> getAllVotes() {
        return mypageService.getAllVotes();
    }
}
