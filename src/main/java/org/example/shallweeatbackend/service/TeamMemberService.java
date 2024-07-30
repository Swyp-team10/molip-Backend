package org.example.shallweeatbackend.service;

import org.example.shallweeatbackend.dto.TeamMemberDTO;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamMember;
import org.example.shallweeatbackend.entity.User;
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


    // 토큰으로 사용자 검증 후 팀메뉴판을 입력받아 team_member 테이블로 저장시키는 로직
    public TeamMemberDTO addMember(String providerId, Long teamBoardId) {
        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 팀 메뉴판 존재하는지 확인
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new IllegalArgumentException("팀 메뉴판을 찾을 수 없습니다."));

        // 팀의 생성자인지 확인(생성자는 팀원으로 존재x)
        if (user.equals(teamBoard.getUser())) {
            throw new IllegalStateException("해당 사용자는 팀의 생성자로, 팀원으로 초대될 수 없습니다.");
        }

        // TeamBoard에 사용자가 이미 포함되어 있는지 확인
        Optional<TeamMember> existingMember = teamMemberRepository.findByUserAndTeamBoard(user, teamBoard);
        if (existingMember.isPresent()) {
            throw new IllegalStateException("사용자는 이미 해당 팀 메뉴판에 포함되어 있습니다.");
        }

        // 팀의 현재 멤버 수 확인
        int currentTeamMembers = teamMemberRepository.countByTeamBoard(teamBoard);
        int maxTeamMembers = teamBoard.getTeamMembersNum() - 1; // 팀 호스트 제외

        // 팀의 최대 인원수를 초과하는지 확인
        if (currentTeamMembers >= maxTeamMembers) {
            throw new IllegalStateException("팀의 최대 인원수를 초과했습니다.");
        }

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


    // 팀 메뉴판 포함 여부 검증
    public boolean isUserInTeam(String providerId, Long teamBoardId) {
        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 팀 메뉴판 존재하는지 확인
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new IllegalArgumentException("팀 메뉴판을 찾을 수 없습니다."));

        // 팀의 생성자인지 확인
        if (user.equals(teamBoard.getUser())) {
            return true;
        }

        // TeamBoard에 사용자가 이미 포함되어 있는지 확인
        Optional<TeamMember> existingMember = teamMemberRepository.findByUserAndTeamBoard(user, teamBoard);
        return existingMember.isPresent();
    }



}
