package org.example.shallweeatbackend.service;

import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.example.shallweeatbackend.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, TeamBoardRepository teamBoardRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.teamBoardRepository = teamBoardRepository;
        this.userRepository = userRepository;
    }

    public VoteDTO createVote(String providerId, Long teamBoardId, String optionSelected) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId).orElseThrow(() -> new TeamBoardNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setTeamBoard(teamBoard);
        vote.setOptionSelected(optionSelected);

        Vote savedVote = voteRepository.save(vote);

        return convertToDTO(savedVote);
    }

    public List<VoteDTO> getVotesByTeamBoardId(Long teamBoardId) {
        return voteRepository.findByTeamBoardTeamBoardId(teamBoardId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VoteDTO convertToDTO(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setVoteId(vote.getVoteId());
        dto.setTeamBoardId(vote.getTeamBoard().getTeamBoardId());
        dto.setUserId(vote.getUser().getUserId());
        dto.setOptionSelected(vote.getOptionSelected());
        return dto;
    }
}
