package org.example.shallweeatbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.shallweeatbackend.dto.TeamBoardMenuDTO;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.repository.MenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardMenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamBoardMenuService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final TeamBoardRepository teamBoardRepository;
    private final TeamBoardMenuRepository teamBoardMenuRepository;

    @Autowired
    public TeamBoardMenuService(UserRepository userRepository, MenuRepository menuRepository, TeamBoardRepository teamBoardRepository, TeamBoardMenuRepository teamBoardMenuRepository){
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.teamBoardRepository = teamBoardRepository;
        this.teamBoardMenuRepository = teamBoardMenuRepository;
    }


    // 팀 메뉴판에 메뉴 생성(추가)
    public TeamBoardMenu addMenuToTeamBoard(Long teamBoardId, Long menuId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("TeamBoard not found"));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));

        TeamBoardMenu teamBoardMenu = new TeamBoardMenu();
        teamBoardMenu.setTeamBoard(teamBoard);
        teamBoardMenu.setMenu(menu);

        return teamBoardMenuRepository.save(teamBoardMenu);
    }

    public TeamBoardMenuDTO convertToDTO2(TeamBoardMenu teamBoardMenu) {
        Menu menu = teamBoardMenu.getMenu();
        TeamBoardMenuDTO dto = new TeamBoardMenuDTO();
        dto.setMenuId(menu.getMenuId());
        dto.setImageUrl(menu.getImageUrl());
        dto.setMenuName(menu.getMenuName());
        dto.setCategoryOptions(menu.getCategoryOptions());
        dto.setTags(menu.getMenuTags().stream()
                .map(menuTag -> menuTag.getTag().getName())
                .collect(Collectors.toList()));
        return dto;
    }

    public List<TeamBoardMenuDTO> getTeamBoardMenuList(Long teamBoardId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("TeamBoard not found"));

        return teamBoard.getTeamBoardMenus().stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());
    }


}
