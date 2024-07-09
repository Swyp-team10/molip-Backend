package org.example.shallweeatbackend.service;


import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.PersonalBoardDTO;
import org.example.shallweeatbackend.dto.TeamBoardDTO;
import org.example.shallweeatbackend.entity.PersonalBoard;
import org.example.shallweeatbackend.entity.PersonalBoardMenu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.exception.PersonalBoardNotFoundException;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class TeamBoardService {

    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamBoardService(TeamBoardRepository teamBoardRepository, UserRepository userRepository) {
        this.teamBoardRepository = teamBoardRepository;
        this.userRepository = userRepository;
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
                .orElseThrow(() -> new PersonalBoardNotFoundException("메뉴판을 찾을 수 없습니다."));

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


    private TeamBoardDTO convertToDTO(TeamBoard teamBoard) {
        TeamBoardDTO dto = new TeamBoardDTO();
        dto.setTeamBoardId(teamBoard.getTeamBoardId());
        dto.setTeamName(teamBoard.getTeamName());
        dto.setTeamMembersNum(teamBoard.getTeamMembersNum());
        dto.setUserName(teamBoard.getUser().getName());
        dto.setUserEmail(teamBoard.getUser().getEmail());
        dto.setCreatedDate(teamBoard.getCreatedDate());
        dto.setModifiedDate(teamBoard.getModifiedDate());
        return dto;
    }



}
