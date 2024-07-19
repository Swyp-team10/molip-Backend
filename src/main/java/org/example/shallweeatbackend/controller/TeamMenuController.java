package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.TeamBoardMenuDTO;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.service.TeamBoardMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<TeamBoardMenuDTO> addMenuToTeamBoard(
            @PathVariable Long teamBoardId,
            @RequestParam Long menuId) {
        TeamBoardMenu teamBoardMenu = teamBoardMenuService.addMenuToTeamBoard(teamBoardId, menuId);
        TeamBoardMenuDTO teamBoardMenuDTO = teamBoardMenuService.convertToDTO2(teamBoardMenu);
        return ResponseEntity.ok(teamBoardMenuDTO);
    }

    // 팀 메뉴판에 담긴 전체 메뉴 목록 조회
    @GetMapping("/{teamBoardId}/teammenuList")
    public ResponseEntity<List<TeamBoardMenuDTO>> showTeamBoardMenuList(@PathVariable Long teamBoardId) {
        List<TeamBoardMenuDTO> teamBoardMenuList = teamBoardMenuService.getTeamBoardMenuList(teamBoardId);
        return ResponseEntity.ok(teamBoardMenuList);
    }

    // 팀 메뉴판에 담긴 전체 메뉴 목록 조회 => 카테고리별 정렬
    @GetMapping("/{teamBoardId}/teammenuList/categories")
    public ResponseEntity<Map<String, List<TeamBoardMenuDTO>>> showGroupedTeamBoardMenuList(@PathVariable Long teamBoardId) {
        Map<String, List<TeamBoardMenuDTO>> groupedTeamBoardMenuList = teamBoardMenuService.getGroupedTeamBoardMenuList(teamBoardId);
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




}
