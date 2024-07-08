package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.TeamBoardDTO;
import org.example.shallweeatbackend.service.TeamBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teamboards")
@RequiredArgsConstructor
public class TeamBoardController {
    private TeamBoardService teamBoardService;

    @Autowired
    public TeamBoardController(TeamBoardService teamBoardService) {
        this.teamBoardService = teamBoardService;
    }

    // 팀 메뉴판 생성
    @PostMapping
    public TeamBoardDTO createTeamBoard(@AuthenticationPrincipal CustomOAuth2User principal, @RequestBody TeamBoardDTO teamBoardDTO) {
        return teamBoardService.createTeamBoard(principal.getProviderId(), teamBoardDTO);
    }

    // 팀 메뉴판 조회 => 수정필요
    @GetMapping("/{teamBoardId}")
    public TeamBoardDTO getTeamBoard(@PathVariable Long teamBoardId){
        return teamBoardService.getTeamBoard(teamBoardId);
    }

    // 팀 메뉴판 수정


    // 팀 메뉴판 삭제




    // 사용자 별 팀 메뉴판 전체 목록 조회 (사용자 본인이 직접 생성했거나 팀원으로 참여하고 있는 경우)
//    @GetMapping("/list/{user_id}")
//    public List<TeamBoardDTO> getTeamBoardList(@PathVariable String user_id) {
//
//    }






}
