package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.entity.*;
import org.example.shallweeatbackend.exception.*;
import org.example.shallweeatbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public List<VoteDTO> createVotes(String providerId, Long teamBoardId, List<Long> menuIds) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new TeamBoardNotFoundException("팀 보드를 찾을 수 없습니다."));

        // 사용자가 해당 팀 보드의 팀원인지 확인 (팀 게시판 생성자 포함)
        if (!teamMemberRepository.existsByTeamBoardAndUser(teamBoard, user) && !teamBoard.getUser().equals(user)) {
            throw new UnauthorizedVoteException("팀 메뉴판에 초대된 사람들만 투표할 수 있습니다.");
        }

        List<VoteDTO> voteDTOs = new ArrayList<>();

        for (Long menuId : menuIds) {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

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
            voteDTOs.add(convertToDTO(savedVote));
        }

        return voteDTOs;
    }

    public List<VoteDTO> updateVotes(String providerId, Long teamBoardId, List<Long> menuIds) {
        User user = userRepository.findByProviderId(providerId);
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new TeamBoardNotFoundException("팀 보드를 찾을 수 없습니다."));

        // 기존 투표 삭제
        List<Vote> existingVotes = voteRepository.findByUserUserIdAndTeamBoardTeamBoardId(user.getUserId(), teamBoardId);
        voteRepository.deleteAll(existingVotes);

        // 새로운 투표 생성
        List<VoteDTO> updatedVoteDTOs = new ArrayList<>();
        for (Long menuId : menuIds) {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

            TeamBoardMenu teamBoardMenu = teamBoardMenuRepository.findByTeamBoardAndMenu(teamBoard, menu)
                    .orElseThrow(() -> new TeamBoardMenuNotFoundException("팀 보드 메뉴를 찾을 수 없습니다."));

            Vote vote = new Vote();
            vote.setUser(user);
            vote.setTeamBoard(teamBoard);
            vote.setMenu(menu);
            vote.setTeamBoardMenu(teamBoardMenu);

            Vote savedVote = voteRepository.save(vote);
            updatedVoteDTOs.add(convertToDTO(savedVote));
        }

        return updatedVoteDTOs;
    }

    public Map<String, Object> getVoteResults(Long teamBoardId, String providerId) {
        TeamBoard teamBoard = teamBoardRepository.findById(teamBoardId)
                .orElseThrow(() -> new TeamBoardNotFoundException("팀 보드를 찾을 수 없습니다."));

        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        List<Vote> votes = voteRepository.findByTeamBoardTeamBoardId(teamBoardId);

        if (votes.isEmpty()) {
            throw new VoteNotFoundException("투표를 찾을 수 없습니다.");
        }

        Map<Long, Long> menuVoteCounts = votes.stream()
                .collect(Collectors.groupingBy(vote -> vote.getMenu().getMenuId(), Collectors.counting()));

        List<Map<String, Object>> voteList = menuVoteCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey)) // 득표수에 따라 내림차순 정렬, 득표수가 같으면 menuId로 오름차순 정렬
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("menuId", entry.getKey());
                    map.put("menuName", menuRepository.findById(entry.getKey()).orElseThrow().getMenuName());
                    map.put("voteValue", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        int completedVotes = (int) votes.stream()
                .map(Vote::getUser)
                .distinct()
                .count();

        int totalMembers = teamMemberRepository.countByTeamBoard(teamBoard);

        boolean isVote = votes.stream()
                .anyMatch(vote -> vote.getUser().equals(user));

        Map<String, Object> result = new HashMap<>();
        result.put("teamName", teamBoard.getTeamName());
        result.put("votes", voteList);
        result.put("투표 완료 인원수", completedVotes);
        result.put("팀 전체 인원수", totalMembers);
        result.put("voteDate", votes.get(0).getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        result.put("isVote", isVote);

        return result;
    }

    public void deleteVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteNotFoundException("투표를 찾을 수 없습니다."));
        voteRepository.delete(vote);
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
