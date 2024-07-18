package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.TeamMemberDTO;
import org.example.shallweeatbackend.service.TeamBoardService;
import org.example.shallweeatbackend.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamMemberController {

    private TeamMemberService teamMemberService;

    @Autowired
    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }


    // 토큰으로 사용자 검증 후 팀메뉴판을 입력받아 team_member 테이블로 저장시키는 로직
    @PostMapping("/invite/{teamBoardId}")
    public TeamMemberDTO addMemberToTeamBoard(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long teamBoardId){
        return teamMemberService.addMember(principal.getProviderId(), teamBoardId);
    }

}
