package org.example.shallweeatbackend.service;


import org.example.shallweeatbackend.dto.TeamBoardDTO;
import org.example.shallweeatbackend.dto.TeamBoardListDTO;
import org.example.shallweeatbackend.entity.*;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.exception.UnauthorizedException;
import org.example.shallweeatbackend.repository.TeamBoardMenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.TeamMemberRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class TeamBoardService {

    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamBoardMenuRepository teamBoardMenuRepository;

    @Autowired
    public TeamBoardService(TeamBoardRepository teamBoardRepository, UserRepository userRepository, TeamMemberRepository teamMemberRepository,
                            TeamBoardMenuRepository teamBoardMenuRepository) {
        this.teamBoardRepository = teamBoardRepository;
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.teamBoardMenuRepository = teamBoardMenuRepository;
    }

    // 팀 메뉴판 생성
    public TeamBoardDTO createTeamBoard(String providerId, String teamName, Integer teamMembersNum, String teamBoardName) {
        if (teamMembersNum < 2 || teamMembersNum > 8) { // 인원수 수정
            throw new IllegalArgumentException("팀 멤버 수는 2명에서 8명 사이여야 합니다.");
        }
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = new TeamBoard();
        teamBoard.setUser(user);

        teamBoard.setTeamName(teamName);
        teamBoard.setTeamMembersNum(teamMembersNum);
        teamBoard.setTeamBoardName(teamBoardName);

        TeamBoard savedTeamBoard = teamBoardRepository.save(teamBoard);

        return convertToDTO(savedTeamBoard);
    }

    // 특정 팀 메뉴판 조회   ****
    public TeamBoardListDTO getTeamBoard(String providerId, Long id) {
        User user = userRepository.findByProviderId(providerId);

        TeamBoard teamBoard = teamBoardRepository.findById(id)
                .orElseThrow(() -> new TeamBoardNotFoundException("메뉴판을 찾을 수 없습니다."));

        boolean hasUserAddedMenu = teamBoardMenuRepository.existsByTeamBoardAndUser(teamBoard, user);

        return convertToDTO2(teamBoard, hasUserAddedMenu);
    }



    // 팀 메뉴판 수정
    public TeamBoardDTO updateTeamBoard(Long id, String providerId, String teamName, Integer teamMembersNum, String teamBoardName) {
        TeamBoard teamBoard = teamBoardRepository.findById(id)
                .orElseThrow(() -> new TeamBoardNotFoundException("메뉴판을 찾을 수 없습니다."));

        User user = userRepository.findByProviderId(providerId);
        boolean isCreator = teamBoard.getUser().equals(user);
        boolean isMember = teamMemberRepository.existsByTeamBoardAndUser(teamBoard, user);

        // teamBoardName이 null이 아니고, 사용자가 생성자(호스트)이거나 팀원인 경우에만 teamBoardName 수정
        if (teamBoardName != null && (isCreator || isMember)) {
            teamBoard.setTeamBoardName(teamBoardName);
        }

        // teamName 또는 teamMembersNum이 null이 아니고, 사용자가 생성자(호스트)인 경우에만 수정
        if (teamName != null || teamMembersNum != null) {
            if (!isCreator) {
                throw new UnauthorizedException("팀 이름과 팀원 수는 생성자만 수정할 수 있습니다.");
            }
            if (teamName != null) {
                teamBoard.setTeamName(teamName);
            }
            if (teamMembersNum != null) {
                teamBoard.setTeamMembersNum(teamMembersNum);
            }
        }

        TeamBoard updatedTeamBoard = teamBoardRepository.save(teamBoard);
        return convertToDTO(updatedTeamBoard);
    }

    // 팀 메뉴판 삭제
    public void deleteTeamBoard(Long id) {
        if (teamBoardRepository.existsById(id)) {
            teamBoardRepository.deleteById(id);
        } else {
            throw new TeamBoardNotFoundException("메뉴판을 찾을 수 없습니다. (메뉴판 ID: " + id + ")");
        }
    }

    // 사용자 별 팀 메뉴판 전체 목록 조회
    public List<TeamBoardListDTO> getUserTeamBoards(String providerId) {
        User user = userRepository.findByProviderId(providerId);
        Long userId = user.getUserId();

        // 사용자가 생성한 팀보드 가져오기
        List<TeamBoardListDTO> createdTeamBoards = teamBoardRepository.findByUserUserId(userId)
                .stream()
                .map(teamBoard -> convertToListDTO(teamBoard, user))
                .collect(Collectors.toList());

        // 사용자가 팀원으로 참여하고 있는 팀보드 가져오기
        List<TeamBoardListDTO> memberTeamBoards = teamMemberRepository.findByUserUserId(userId)
                .stream()
                .map(TeamMember::getTeamBoard)
                .map(teamBoard -> convertToListDTO(teamBoard, user))
                .collect(Collectors.toList());

        createdTeamBoards.addAll(memberTeamBoards);

        createdTeamBoards.sort(Comparator.comparing(TeamBoardListDTO::getTeamBoardId).reversed());

        return createdTeamBoards;
    }

    private TeamBoardListDTO convertToListDTO(TeamBoard teamBoard, User user) {
        TeamBoardListDTO dto = new TeamBoardListDTO();
        dto.setTeamBoardId(teamBoard.getTeamBoardId());
        dto.setTeamBoardName(teamBoard.getTeamBoardName());
        dto.setTeamName(teamBoard.getTeamName());
        dto.setTeamMembersNum(teamBoard.getTeamMembersNum());
        dto.setCreatedDate(teamBoard.getCreatedDate());
        dto.setModifiedDate(teamBoard.getModifiedDate());
        dto.setHasUserAddedMenu(teamBoardMenuRepository.existsByTeamBoardAndUser(teamBoard, user));
        return dto;
    }



    private TeamBoardDTO convertToDTO(TeamBoard teamBoard) {
        TeamBoardDTO dto = new TeamBoardDTO();
        dto.setTeamBoardId(teamBoard.getTeamBoardId());
        dto.setTeamBoardName(teamBoard.getTeamBoardName());
        dto.setTeamMembersNum(teamBoard.getTeamMembersNum());
        dto.setTeamName(teamBoard.getTeamName());
        dto.setUserId(teamBoard.getUser().getUserId());
        dto.setUserName(teamBoard.getUser().getName());
        dto.setUserEmail(teamBoard.getUser().getEmail());
        dto.setCreatedDate(teamBoard.getCreatedDate());
        dto.setModifiedDate(teamBoard.getModifiedDate());
        return dto;
    }

    private TeamBoardListDTO convertToDTO2(TeamBoard teamBoard, boolean hasUserAddedMenu) {
        TeamBoardListDTO dto = new TeamBoardListDTO();
        dto.setTeamBoardId(teamBoard.getTeamBoardId());
        dto.setTeamBoardName(teamBoard.getTeamBoardName());
        dto.setTeamName(teamBoard.getTeamName());
        dto.setTeamMembersNum(teamBoard.getTeamMembersNum());
        dto.setCreatedDate(teamBoard.getCreatedDate());
        dto.setModifiedDate(teamBoard.getModifiedDate());
        dto.setHasUserAddedMenu(hasUserAddedMenu);
        return dto;
    }







}
