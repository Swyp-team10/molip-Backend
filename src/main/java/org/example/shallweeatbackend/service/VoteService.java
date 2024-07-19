package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.exception.MenuNotFoundException;
import org.example.shallweeatbackend.exception.TeamBoardMenuNotFoundException;
import org.example.shallweeatbackend.exception.VoteNotFoundException;
import org.example.shallweeatbackend.exception.VoteLimitExceededException;
import org.example.shallweeatbackend.exception.DuplicateVoteException;
import org.example.shallweeatbackend.exception.UnauthorizedVoteException;
import org.example.shallweeatbackend.repository.MenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardMenuRepository;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.TeamMemberRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.example.shallweeatbackend.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final TeamBoardRepository teamBoardRepository;
    private final TeamBoardMenuRepository teamBoardMenuRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    private static final int MAX_VOTES_PER_USER = 3;

    public VoteDTO createVote(String providerId, Long teamBoardId, Long menuId) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new TeamBoardNotFoundException("팀 보드를 찾을 수 없습니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        // 사용자가 해당 팀 보드의 팀원인지 확인
        if (!teamMemberRepository.existsByTeamBoardAndUser(teamBoard, user)) {
            throw new UnauthorizedVoteException("팀 메뉴판에 초대된 사람들만 투표할 수 있습니다.");
        }

        // 사용자가 해당 팀 보드에서 이미 3개의 메뉴에 투표했는지 확인
        long voteCount = voteRepository.countByUserUserIdAndTeamBoardTeamBoardId(user.getUserId(), teamBoardId);
        if (voteCount >= MAX_VOTES_PER_USER) {
            throw new VoteLimitExceededException("한 사람당 최대 3개의 메뉴에만 투표할 수 있습니다.");
        }

        // 사용자가 이미 해당 메뉴에 투표했는지 확인
        boolean alreadyVoted = voteRepository.existsByUserUserIdAndTeamBoardTeamBoardIdAndMenuMenuId(user.getUserId(), teamBoardId, menuId);
        if (alreadyVoted) {
            throw new DuplicateVoteException("이미 이 메뉴에 투표하셨습니다.");
        }

        TeamBoardMenu teamBoardMenu = teamBoardMenuRepository.findByTeamBoardAndMenu(teamBoard, menu)
                .orElseThrow(() -> new TeamBoardMenuNotFoundException("팀 보드 메뉴를 찾을 수 없습니다."));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setTeamBoard(teamBoard);
        vote.setMenu(menu);
        vote.setTeamBoardMenu(teamBoardMenu);

        Vote savedVote = voteRepository.save(vote);

        return convertToDTO(savedVote);
    }

    public VoteDTO updateVote(Long voteId, Long teamBoardId, Long menuId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteNotFoundException("투표를 찾을 수 없습니다."));

        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new TeamBoardNotFoundException("팀 보드를 찾을 수 없습니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        vote.setTeamBoard(teamBoard);
        vote.setMenu(menu);

        TeamBoardMenu teamBoardMenu = teamBoardMenuRepository.findByTeamBoardAndMenu(teamBoard, menu)
                .orElseThrow(() -> new TeamBoardMenuNotFoundException("팀 보드 메뉴를 찾을 수 없습니다."));

        vote.setTeamBoardMenu(teamBoardMenu);

        Vote updatedVote = voteRepository.save(vote);

        return convertToDTO(updatedVote);
    }

    public void deleteVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteNotFoundException("투표를 찾을 수 없습니다."));
        voteRepository.delete(vote);
    }

    public List<VoteDTO> getVotesByTeamBoardId(Long teamBoardId) {
        return voteRepository.findByTeamBoardTeamBoardId(teamBoardId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VoteDTO> getVotesByMenuId(Long menuId) {
        return voteRepository.findByMenuMenuId(menuId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long countVotesByMenuId(Long menuId) {
        return voteRepository.countByMenuMenuId(menuId);
    }

    public Map<String, Long> getMenuNameAndVoteCountByMenuId(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
        long voteCount = voteRepository.countByMenuMenuId(menuId);
        Map<String, Long> result = new HashMap<>();
        result.put(menu.getMenuName(), voteCount);
        return result;
    }

    private VoteDTO convertToDTO(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        dto.setTeamBoardId(vote.getTeamBoard().getTeamBoardId());
        dto.setMenuId(vote.getMenu().getMenuId());
        dto.setUserId(vote.getUser().getUserId());
        dto.setTeamBoardMenuId(vote.getTeamBoardMenu().getTeamBoardMenuId());
        dto.setCreatedDate(vote.getCreatedDate());
        dto.setMenuName(vote.getMenu().getMenuName());
        return dto;
    }
}
