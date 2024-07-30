package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.TeamMemberDTO;
import org.example.shallweeatbackend.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeamMemberController {

    private TeamMemberService teamMemberService;

    @Autowired
    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }


    // 팀원 초대(팀 메뉴판에 팀원 추가)
    @PostMapping("/invite/{teamBoardId}")
    public TeamMemberDTO addMemberToTeamBoard(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long teamBoardId){
        return teamMemberService.addMember(principal.getProviderId(), teamBoardId);
    }

    // 팀 메뉴판에 참여 여부
    @GetMapping("/invite/{teamBoardId}")
    public Map<String, Boolean> isUserInTeamBoard(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long teamBoardId) {
        boolean isTeam = teamMemberService.isUserInTeam(principal.getProviderId(), teamBoardId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isTeam", isTeam);
        return response;
    }
}
