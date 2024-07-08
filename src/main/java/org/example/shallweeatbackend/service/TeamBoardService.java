package org.example.shallweeatbackend.service;


import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.PersonalBoardDTO;
import org.example.shallweeatbackend.dto.TeamBoardDTO;
import org.example.shallweeatbackend.entity.PersonalBoard;
import org.example.shallweeatbackend.entity.PersonalBoardMenu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.exception.PersonalBoardNotFoundException;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamBoardService {

    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;
    public TeamBoardDTO createTeamBoard(String providerId, TeamBoardDTO teamBoardDTO) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = new TeamBoard();
        teamBoard.setUser(user);

        teamBoard.setTeamName(teamBoardDTO.getTeamName());
        teamBoard.setTeamMembersNum(teamBoardDTO.getTeamMembersNum());
        teamBoard.setTeamBoardName(teamBoardDTO.getTeamBoardName());

        TeamBoard savedTeamBoard = teamBoardRepository.save(teamBoard);

        return convertToDTO(savedTeamBoard);

    }

    public TeamBoardDTO getTeamBoard(long teamBoardId){
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new PersonalBoardNotFoundException("메뉴판을 찾을 수 없습니다."));

        return convertToDTO(teamBoard);

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
