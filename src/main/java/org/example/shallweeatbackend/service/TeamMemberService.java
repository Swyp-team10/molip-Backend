package org.example.shallweeatbackend.service;

import org.example.shallweeatbackend.dto.TeamMemberDTO;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamMember;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.exception.UserNotFoundException;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.TeamMemberRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TeamMemberService {

    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamMemberService(TeamBoardRepository teamBoardRepository, UserRepository userRepository, TeamMemberRepository teamMemberRepository) {
        this.teamBoardRepository = teamBoardRepository;
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
    }


    public TeamMemberDTO addMember(String providerId, Long teamBoardId) {
        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 팀 메뉴판 존재하는지 확인
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new IllegalArgumentException("팀 메뉴판을 찾을 수 없습니다."));

        // TeamBoard에 사용자가 이미 포함되어 있는지 확인
        Optional<TeamMember> existingMember = teamMemberRepository.findByUserAndTeamBoard(user, teamBoard);
        if (existingMember.isPresent()) {
            throw new IllegalStateException("사용자는 이미 해당 팀 메뉴판에 포함되어 있습니다.");
        }

        // teamMemberNum 관련 예외 처리 (호스트 포함 인원)

        TeamMember teamMember = new TeamMember();
        teamMember.setUser(user);
        teamMember.setTeamBoard(teamBoard);
        teamMemberRepository.save(teamMember);

        return new TeamMemberDTO(
                teamMember.getTeamMemberId(),
                teamBoard.getTeamBoardId(),
                teamBoard.getTeamBoardName(),
                user.getUserId(),
                user.getName()
        );
    }


}
