package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.example.shallweeatbackend.exception.TeamBoardNotFoundException;
import org.example.shallweeatbackend.exception.VoteNotFoundException;
import org.example.shallweeatbackend.repository.TeamBoardRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.example.shallweeatbackend.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;

    public VoteDTO createVote(String providerId, Long teamBoardId, String optionSelected) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new TeamBoardNotFoundException("팀 메뉴판을 찾을 수 없습니다."));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setTeamBoard(teamBoard);
        vote.setOptionSelected(optionSelected);

        Vote savedVote = voteRepository.save(vote);

        return convertToDTO(savedVote);
    }

    public VoteDTO updateVote(Long voteId, String optionSelected) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteNotFoundException("투표를 찾을 수 없습니다."));

        vote.setOptionSelected(optionSelected);

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

    public Map<String, Long> getVoteResultsByTeamBoardId(Long teamBoardId) {
        return voteRepository.findByTeamBoardTeamBoardId(teamBoardId)
                .stream()
                .collect(Collectors.groupingBy(Vote::getOptionSelected, Collectors.counting()));
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
