package org.example.shallweeatbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.shallweeatbackend.dto.TeamBoardMenuDTO;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.repository.MenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardMenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamBoardMenuService {
    private final MenuRepository menuRepository;
    private final TeamBoardRepository teamBoardRepository;
    private final TeamBoardMenuRepository teamBoardMenuRepository;

    @Autowired
    public TeamBoardMenuService(MenuRepository menuRepository, TeamBoardRepository teamBoardRepository, TeamBoardMenuRepository teamBoardMenuRepository){
        this.menuRepository = menuRepository;
        this.teamBoardRepository = teamBoardRepository;
        this.teamBoardMenuRepository = teamBoardMenuRepository;
    }


    // 팀 메뉴판에 메뉴 생성(추가)
//    public TeamBoardMenu addMenuToTeamBoard(Long teamBoardId, Long menuId) {
//        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
//                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));
//
//        Menu menu = menuRepository.findById(menuId)
//                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));
//
//        TeamBoardMenu teamBoardMenu = new TeamBoardMenu();
//        teamBoardMenu.setTeamBoard(teamBoard);
//        teamBoardMenu.setMenu(menu);
//
//        return teamBoardMenuRepository.save(teamBoardMenu);
//    }
    public List<TeamBoardMenu> addMenusToTeamBoard(Long teamBoardId, List<Long> menuIds) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new EntityNotFoundException("일부 메뉴를 찾을 수 없습니다.");
        }

        List<TeamBoardMenu> teamBoardMenus = menus.stream()
                .map(menu -> {
                    TeamBoardMenu teamBoardMenu = new TeamBoardMenu();
                    teamBoardMenu.setTeamBoard(teamBoard);
                    teamBoardMenu.setMenu(menu);
                    return teamBoardMenu;
                })
                .collect(Collectors.toList());

        return teamBoardMenuRepository.saveAll(teamBoardMenus);
    }

    public TeamBoardMenuDTO convertToDTO2(TeamBoardMenu teamBoardMenu) {
        Menu menu = teamBoardMenu.getMenu();
        TeamBoard teamBoard = teamBoardMenu.getTeamBoard(); // TeamBoard 객체 가져오기

        TeamBoardMenuDTO dto = new TeamBoardMenuDTO();
        dto.setTeamBoardMenuId(teamBoardMenu.getTeamBoardMenuId());
        dto.setMenuId(menu.getMenuId());
        dto.setImageUrl(menu.getImageUrl());
        dto.setMenuName(menu.getMenuName());
        dto.setCategoryOptions(menu.getCategoryOptions());
        dto.setTags(menu.getMenuTags().stream()
                .map(menuTag -> menuTag.getTag().getName())
                .collect(Collectors.toList()));
        dto.setTeamBoardName(teamBoard.getTeamBoardName()); // teamBoardName 설정

        return dto;
    }

    // 팀 메뉴판에 담긴 전체 메뉴 목록 조회
    public List<TeamBoardMenuDTO> getTeamBoardMenuList(Long teamBoardId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        return teamBoard.getTeamBoardMenus().stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());
    }

    // 팀 메뉴판에 담긴 전체 메뉴 목록 조회 => 카테고리별 정렬

    public Map<String, List<TeamBoardMenuDTO>> getGroupedTeamBoardMenuList(Long teamBoardId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        return teamBoard.getTeamBoardMenus().stream()
                .map(this::convertToDTO2)
                .collect(Collectors.groupingBy(TeamBoardMenuDTO::getCategoryOptions));
    }

    public TeamBoardMenuDTO getTeamBoardMenu(Long teamBoardId, Long teamBoardMenuId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        TeamBoardMenu teamBoardMenu = teamBoardMenuRepository.findById(teamBoardMenuId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판의 메뉴를 찾을 수 없습니다."));

        if (!teamBoardMenu.getTeamBoard().getTeamBoardId().equals(teamBoardId)) {
            throw new IllegalArgumentException("이 메뉴는 해당 팀 메뉴판에 포함되어 있지 않습니다.");
        }

        return convertToDTO2(teamBoardMenu);
    }

}
