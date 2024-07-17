package org.example.shallweeatbackend.service;


import org.example.shallweeatbackend.dto.TeamBoardDTO;
import org.example.shallweeatbackend.entity.*;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.TeamMemberRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class TeamBoardService {

    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamBoardService(TeamBoardRepository teamBoardRepository, UserRepository userRepository, TeamMemberRepository teamMemberRepository) {
        this.teamBoardRepository = teamBoardRepository;
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    // 팀 메뉴판 생성
    public TeamBoardDTO createTeamBoard(String providerId, String teamName, Integer teamMembersNum, String teamBoardName) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = new TeamBoard();
        teamBoard.setUser(user);

        teamBoard.setTeamName(teamName);
        teamBoard.setTeamMembersNum(teamMembersNum);
        teamBoard.setTeamBoardName(teamBoardName);

        TeamBoard savedTeamBoard = teamBoardRepository.save(teamBoard);

        return convertToDTO(savedTeamBoard);
    }

    // 특정 팀 메뉴판 조회
    public TeamBoardDTO getTeamBoard(Long id){
        TeamBoard teamBoard = teamBoardRepository.findById(id)
                .orElseThrow(() -> new TeamBoardNotFoundException("메뉴판을 찾을 수 없습니다."));

        return convertToDTO(teamBoard);
    }

    // 팀 메뉴판 수정
    public TeamBoardDTO updateTeamBoard(Long id, String teamName, Integer teamMembersNum, String teamBoardName){
        TeamBoard teamBoard = teamBoardRepository.findById(id)
                .orElseThrow(() -> new TeamBoardNotFoundException("메뉴판을 찾을 수 없습니다."));

        teamBoard.setTeamName(teamName);
        teamBoard.setTeamMembersNum(teamMembersNum);
        teamBoard.setTeamBoardName(teamBoardName);

        TeamBoard updatedTeamBoard = teamBoardRepository.save(teamBoard);
        return convertToDTO(updatedTeamBoard);

    }

    // 팀 메뉴판 삭제
    public void deleteTeamBoard(Long id){
        if (teamBoardRepository.existsById(id)) {
            teamBoardRepository.deleteById(id);
        } else {
            throw new TeamBoardNotFoundException("메뉴판을 찾을 수 없습니다. (메뉴판 ID: " + id + ")");
        }
    }

    // 사용자 별 팀 메뉴판 전체 목록 조회
    public List<TeamBoardDTO> getUserTeamBoards(String providerId) {
        User user = userRepository.findByProviderId(providerId);
        Long userId = user.getUserId();

        // 사용자가 생성한 팀보드 가져오기
        List<TeamBoardDTO> createdTeamBoards = teamBoardRepository.findByUserUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 사용자가 팀원으로 참여하고 있는 팀보드 가져오기
        List<TeamBoardDTO> memberTeamBoards = teamMemberRepository.findByUserUserId(userId)
                .stream()
                .map(TeamMember::getTeamBoard)
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        createdTeamBoards.addAll(memberTeamBoards);
        return createdTeamBoards;
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



}
