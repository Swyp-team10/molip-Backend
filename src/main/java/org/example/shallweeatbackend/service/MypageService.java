package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.UserDTO;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.example.shallweeatbackend.exception.UserNotFoundException;
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

        Map<Long, Map<String, Object>> voteResults = new HashMap<>();
        for (Vote vote : userVotes) {
            Long teamBoardId = vote.getTeamBoard().getTeamBoardId();
            String teamName = vote.getTeamBoard().getTeamName();

            if (!voteResults.containsKey(teamBoardId)) {
                Map<String, Object> voteResult = new HashMap<>();
                voteResult.put("teamName", teamName);
                voteResult.put("voteDate", vote.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd(EEE)")));
                voteResult.put("voteItems", new HashSet<String>());
                voteResults.put(teamBoardId, voteResult);
            }

            ((Set<String>) voteResults.get(teamBoardId).get("voteItems")).add(vote.getMenu().getMenuName());
        }

        return voteResults.entrySet().stream()
                .sorted(Map.Entry.<Long, Map<String, Object>>comparingByKey(Comparator.reverseOrder())) // 팀 보드 ID 기준 최신순 정렬
                .map(Map.Entry::getValue)
                .map(result -> {
                    List<String> sortedVoteItems = ((Set<String>) result.get("voteItems")).stream()
                            .sorted()
                            .collect(Collectors.toList());
                    result.put("voteItems", sortedVoteItems);
                    return result;
                })
                .collect(Collectors.toList());
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