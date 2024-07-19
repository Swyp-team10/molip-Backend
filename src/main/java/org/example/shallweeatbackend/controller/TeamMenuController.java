package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.*;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.service.TeamBoardMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teamboards")
@RequiredArgsConstructor
public class TeamMenuController {

    private TeamBoardMenuService teamBoardMenuService;

    @Autowired
    public TeamMenuController(TeamBoardMenuService teamBoardMenuService){
        this.teamBoardMenuService = teamBoardMenuService;
    }

    // 메뉴를 팀 메뉴판에 추가
    @PostMapping("/{teamBoardId}/teammenus")
    public ResponseEntity<List<TeamBoardMenuDTO>> addMenusToTeamBoard(@AuthenticationPrincipal CustomOAuth2User principal,
            @PathVariable Long teamBoardId,
            @RequestBody AddMenusToTeamBoardRequest request) {
        List<TeamBoardMenu> teamBoardMenus = teamBoardMenuService.addMenusToTeamBoard(principal.getProviderId(), teamBoardId, request.getMenuIds());
        List<TeamBoardMenuDTO> teamBoardMenuDTOs = teamBoardMenus.stream()
                .map(teamBoardMenuService::convertToDTO2)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teamBoardMenuDTOs);
    }

    // 팀 메뉴판에 담긴 전체 메뉴 목록 조회
    @GetMapping("/{teamBoardId}/teammenuList")
    public ResponseEntity<List<TeamBoardMenuDTO>> showTeamBoardMenuList(@PathVariable Long teamBoardId) {
        List<TeamBoardMenuDTO> teamBoardMenuList = teamBoardMenuService.getTeamBoardMenuList(teamBoardId);
        return ResponseEntity.ok(teamBoardMenuList);
    }

    // 팀 메뉴판에 담긴 전체 메뉴 목록 조회 => 카테고리별 정렬
    @GetMapping("/{teamBoardId}/teammenuList/categories")
    public ResponseEntity<List<CategoryMenuDTO>> showGroupedTeamBoardMenuList(@PathVariable Long teamBoardId) {
        List<CategoryMenuDTO> groupedTeamBoardMenuList = teamBoardMenuService.getGroupedTeamBoardMenuList(teamBoardId);
        return ResponseEntity.ok(groupedTeamBoardMenuList);
    }

    // 팀 메뉴판에 담긴 특정 메뉴 조회
    @GetMapping("/{teamBoardId}/teammenuList/{teamBoardMenuId}")
    public ResponseEntity<TeamBoardMenuDTO> getTeamBoardMenu(
            @PathVariable Long teamBoardId,
            @PathVariable Long teamBoardMenuId) {
        TeamBoardMenuDTO teamBoardMenuDTO = teamBoardMenuService.getTeamBoardMenu(teamBoardId, teamBoardMenuId);
        return ResponseEntity.ok(teamBoardMenuDTO);
    }

    // 해당 팀 게시판에 메뉴를 추가한 현재 인원수
    @GetMapping("/{teamBoardId}/countAdded")
    public CountMembersNumDTO getTeamBoardDetails(@PathVariable("teamBoardId") Long teamBoardId) {
        return teamBoardMenuService.getTeamBoardDetails(teamBoardId);
    }





}
