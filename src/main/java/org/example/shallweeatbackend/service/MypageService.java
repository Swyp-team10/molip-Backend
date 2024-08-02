package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.UserDTO;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.example.shallweeatbackend.exception.UserNotFoundException;
import org.example.shallweeatbackend.repository.MenuRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.example.shallweeatbackend.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    public UserDTO getUserInfo(String providerId) {
        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return convertToUserDTO(user);
    }

    public List<Map<String, Object>> getUserVotes(String providerId) {
        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 사용자의 모든 투표를 조회합니다.
        List<Vote> userVotes = voteRepository.findByUserUserId(user.getUserId());

        // 팀 보드별로 투표를 그룹화
        Map<Long, List<Vote>> votesGroupedByTeamBoard = userVotes.stream()
                .collect(Collectors.groupingBy(vote -> vote.getTeamBoard().getTeamBoardId()));

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<Long, List<Vote>> entry : votesGroupedByTeamBoard.entrySet()) {
            List<Vote> votes = entry.getValue();
            TeamBoard teamBoard = votes.get(0).getTeamBoard();

            Map<Long, Long> menuVoteCounts = votes.stream()
                    .collect(Collectors.groupingBy(vote -> vote.getMenu().getMenuId(), Collectors.counting()));

            // 득표수 내림차순, 득표수가 같으면 메뉴ID 오름차순 정렬
            List<Map<String, Object>> voteItems = menuVoteCounts.entrySet().stream()
                    .map(menuEntry -> {
                        Menu menu = menuRepository.findById(menuEntry.getKey())
                                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));
                        Map<String, Object> voteItem = new HashMap<>();
                        voteItem.put("menuId", menu.getMenuId());
                        voteItem.put("menuName", menu.getMenuName());
                        voteItem.put("voteValue", menuEntry.getValue());
                        return voteItem;
                    })
                    .sorted(Comparator.comparing((Map<String, Object> item) -> (Long) item.get("voteValue")).reversed()
                            .thenComparing(item -> (Long) item.get("menuId")))
                    .collect(Collectors.toList());

            Map<String, Object> teamBoardVotes = new HashMap<>();
            teamBoardVotes.put("teamName", teamBoard.getTeamName());
            teamBoardVotes.put("voteDate", votes.get(0).getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            teamBoardVotes.put("votes", voteItems);

            result.add(teamBoardVotes);
        }

        return result;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProviderId(user.getProviderId());
        dto.setRole(user.getRole());
        return dto;
    }
}
