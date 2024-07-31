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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
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
        //teamBoard.setUser(user);

        boolean isTeamMember = teamBoard.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getUser().equals(user));

        if (!isTeamMember && !teamBoard.getUser().equals(user)) {
            throw new AccessDeniedException("사용자는 이 팀 보드에 대한 권한이 없습니다.");
        }

        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new EntityNotFoundException("일부 메뉴를 찾을 수 없습니다.");
        }

        List<TeamBoardMenu> teamBoardMenus = menus.stream()
                .map(menu -> {
                    TeamBoardMenu teamBoardMenu = new TeamBoardMenu();
                    teamBoardMenu.setTeamBoard(teamBoard);
                    teamBoardMenu.setMenu(menu);
                    teamBoardMenu.setUser(user); // 사용자
                    return teamBoardMenu;
                })
                .collect(Collectors.toList());

        return teamBoardMenuRepository.saveAll(teamBoardMenus);
    }

    // 팀 메뉴판에 추가한 메뉴 수정
    public List<TeamBoardMenu> updateMenusInTeamBoard(String providerId, Long teamBoardId, List<Long> menuIds) {
        User user = userRepository.findByProviderId(providerId);

        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new EntityNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        // 팀 메뉴판의 기존 메뉴들을 삭제
        teamBoardMenuRepository.deleteByTeamBoard(teamBoard);

        // 새로운 메뉴들을 추가
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new EntityNotFoundException("일부 메뉴를 찾을 수 없습니다.");
        }

        // 새로운 팀 메뉴판 메뉴를 생성합니다.
        List<TeamBoardMenu> teamBoardMenus = menus.stream()
                .map(menu -> {
                    TeamBoardMenu teamBoardMenu = new TeamBoardMenu();
                    teamBoardMenu.setTeamBoard(teamBoard);
                    teamBoardMenu.setMenu(menu);
                    teamBoardMenu.setUser(user); // 사용자
                    return teamBoardMenu;
                })
                .collect(Collectors.toList());

        // 새로운 메뉴 저장
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

        // 원하는 카테고리 순서
        List<String> categoryOrder = Arrays.asList("한식", "중식", "일식", "양식", "인도/베트남/태국", "멕시코", "육류/해산물");

        // 카테고리 순서에 따라 정렬 및 데이터가 있는 카테고리만 응답
        return categoryOrder.stream()
                .filter(category -> groupedMenu.containsKey(category)) // 데이터가 있는 카테고리만 필터링
                .map(category -> {
                    List<TeamBoardMenuDTO> menus = groupedMenu.get(category);

                    // teamboardmenu 식별자가 작은 것만 남기기
                    Map<String, TeamBoardMenuDTO> uniqueMenuMap = menus.stream()
                            .collect(Collectors.toMap(
                                    TeamBoardMenuDTO::getMenuName,
                                    Function.identity(),
                                    (existing, replacement) -> existing.getMenuId() < replacement.getMenuId() ? existing : replacement
                            ));

                    List<CategoryMenuDTO.MenuDTO> menuDTOList = uniqueMenuMap.values().stream().map(menuDTO -> {
                        CategoryMenuDTO.MenuDTO menuItemDTO = new CategoryMenuDTO.MenuDTO();
                        menuItemDTO.setMenuId(menuDTO.getMenuId());
                        menuItemDTO.setImageUrl(menuDTO.getImageUrl());
                        menuItemDTO.setMenuName(menuDTO.getMenuName());
                        menuItemDTO.setTags(menuDTO.getTags());
                        return menuItemDTO;
                    }).collect(Collectors.toList());

                    CategoryMenuDTO categoryMenuDTO = new CategoryMenuDTO();
                    categoryMenuDTO.setCategory(category);
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
