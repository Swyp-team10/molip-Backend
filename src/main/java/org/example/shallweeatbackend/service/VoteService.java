package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.example.shallweeatbackend.exception.TeamBoardMenuNotFoundException;
import org.example.shallweeatbackend.exception.MenuNotFoundException;
import org.example.shallweeatbackend.exception.VoteNotFoundException;
import org.example.shallweeatbackend.repository.*;
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

    // 수정: createVote 메서드가 세 개의 매개변수를 받도록 수정
    public VoteDTO createVote(String providerId, Long teamBoardMenuId, Long menuId) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoardMenu teamBoardMenu = teamBoardMenuRepository.findById(teamBoardMenuId)
                .orElseThrow(() -> new TeamBoardMenuNotFoundException("팀 메뉴를 찾을 수 없습니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setTeamBoardMenu(teamBoardMenu);
        vote.setMenu(menu);
        vote.setTeamBoard(teamBoardMenu.getTeamBoard());

        Vote savedVote = voteRepository.save(vote);

        return convertToDTO(savedVote);
    }

    public VoteDTO updateVote(Long voteId, Long teamBoardMenuId, Long menuId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteNotFoundException("투표를 찾을 수 없습니다."));

        TeamBoardMenu teamBoardMenu = teamBoardMenuRepository.findById(teamBoardMenuId)
                .orElseThrow(() -> new TeamBoardMenuNotFoundException("팀 메뉴를 찾을 수 없습니다."));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        vote.setTeamBoardMenu(teamBoardMenu);
        vote.setMenu(menu);

        Vote updatedVote = voteRepository.save(vote);

        return convertToDTO(updatedVote);
    }

    public void deleteVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteNotFoundException("투표를 찾을 수 없습니다."));
        voteRepository.delete(vote);
    }

    public List<VoteDTO> getVotesByTeamBoardMenuId(Long teamBoardMenuId) {
        return voteRepository.findByTeamBoardMenuTeamBoardMenuId(teamBoardMenuId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getVoteResultsByTeamBoardMenuId(Long teamBoardMenuId) {
        return voteRepository.findByTeamBoardMenuTeamBoardMenuId(teamBoardMenuId)
                .stream()
                .collect(Collectors.groupingBy(vote -> vote.getMenu().getMenuName(), Collectors.counting()));
    }

    public Map<String, Long> getTotalVotesByTeamBoardId(Long teamBoardId) {
        List<TeamBoardMenu> teamBoardMenus = teamBoardMenuRepository.findByTeamBoardTeamBoardId(teamBoardId);
        Map<String, Long> totalVotes = new HashMap<>();

        for (TeamBoardMenu teamBoardMenu : teamBoardMenus) {
            Long menuId = teamBoardMenu.getMenu().getMenuId();
            Long voteCount = voteRepository.countByMenuMenuId(menuId);
            totalVotes.put(teamBoardMenu.getMenu().getMenuName(), voteCount);
        }

        return totalVotes;
    }

    private VoteDTO convertToDTO(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        dto.setTeamBoardId(vote.getTeamBoard().getTeamBoardId());
        dto.setTeamBoardMenuId(vote.getTeamBoardMenu().getTeamBoardMenuId());
        dto.setMenuId(vote.getMenu().getMenuId());
        dto.setUserId(vote.getUser().getUserId());
        return dto;
    }
}
