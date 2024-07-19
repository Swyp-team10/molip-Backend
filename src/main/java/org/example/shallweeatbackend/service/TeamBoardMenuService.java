package org.example.shallweeatbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.shallweeatbackend.dto.CategoryMenuDTO;
import org.example.shallweeatbackend.dto.CountMembersNumDTO;
import org.example.shallweeatbackend.dto.TeamBoardMenuDTO;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.repository.MenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardMenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.UserRepository;
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

    private final UserRepository userRepository;

    @Autowired
    public TeamBoardMenuService(MenuRepository menuRepository, TeamBoardRepository teamBoardRepository, TeamBoardMenuRepository teamBoardMenuRepository, UserRepository userRepository){
        this.menuRepository = menuRepository;
        this.teamBoardRepository = teamBoardRepository;
        this.teamBoardMenuRepository = teamBoardMenuRepository;
        this.userRepository = userRepository;
    }


    // 팀 메뉴판에 메뉴 생성(추가)
    public List<TeamBoardMenu> addMenusToTeamBoard(String providerId, Long teamBoardId, List<Long> menuIds) {
        User user = userRepository.findByProviderId(providerId);

        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));
        teamBoard.setUser(user);

        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new EntityNotFoundException("일부 메뉴를 찾을 수 없습니다.");
        }

        List<TeamBoardMenu> teamBoardMenus = menus.stream()
                .map(menu -> {
                    TeamBoardMenu teamBoardMenu = new TeamBoardMenu();
                    teamBoardMenu.setTeamBoard(teamBoard);
                    teamBoardMenu.setMenu(menu);
                    teamBoardMenu.setUser(user); // 여기서 user를 설정합니다.
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
    public List<CategoryMenuDTO> getGroupedTeamBoardMenuList(Long teamBoardId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        // 메뉴를 카테고리별로 그룹화
        Map<String, List<TeamBoardMenuDTO>> groupedMenu = teamBoard.getTeamBoardMenus().stream()
                .map(this::convertToDTO2)
                .collect(Collectors.groupingBy(TeamBoardMenuDTO::getCategoryOptions));

        // 변환된 데이터를 새로운 DTO 형태로 변환
        return groupedMenu.entrySet().stream().map(entry -> {
            CategoryMenuDTO categoryMenuDTO = new CategoryMenuDTO();
            categoryMenuDTO.setCategory(entry.getKey());

            List<CategoryMenuDTO.MenuDTO> menuDTOList = entry.getValue().stream().map(menuDTO -> {
                CategoryMenuDTO.MenuDTO menuItemDTO = new CategoryMenuDTO.MenuDTO();
                menuItemDTO.setMenuId(menuDTO.getMenuId());
                menuItemDTO.setImageUrl(menuDTO.getImageUrl());
                menuItemDTO.setMenuName(menuDTO.getMenuName());
                menuItemDTO.setTags(menuDTO.getTags());
                return menuItemDTO;
            }).collect(Collectors.toList());

            categoryMenuDTO.setMenu(menuDTOList);
            return categoryMenuDTO;
        }).collect(Collectors.toList());
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

    // 해당 팀 게시판에 메뉴를 추가한 현재 인원수
    public CountMembersNumDTO getTeamBoardDetails(Long teamBoardId) {
        Long addedMenuUserCount = teamBoardRepository.countDistinctUsersByTeamBoardId(teamBoardId);
        Integer teamMembersNum = teamBoardRepository.findTeamMembersNumByTeamBoardId(teamBoardId);
        return new CountMembersNumDTO(addedMenuUserCount, teamMembersNum);
    }

}
