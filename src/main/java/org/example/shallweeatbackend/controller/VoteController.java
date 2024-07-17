package org.example.shallweeatbackend.controller;

import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    // 투표 생성
    @PostMapping
    public ResponseEntity<VoteDTO> createVote(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam Long teamBoardMenuId) {
        VoteDTO voteDTO = voteService.createVote(principal.getProviderId(), teamBoardMenuId);
        return new ResponseEntity<>(voteDTO, HttpStatus.CREATED);
    }

    // 투표 수정
    @PatchMapping("/{voteId}")
    public ResponseEntity<VoteDTO> updateVote(@PathVariable Long voteId) {
        VoteDTO voteDTO = voteService.updateVote(voteId);
        return ResponseEntity.ok(voteDTO);
    }

    // 투표 조회
    @GetMapping("/{teamBoardMenuId}")
    public ResponseEntity<List<VoteDTO>> getVotesByTeamBoardMenuId(@PathVariable Long teamBoardMenuId) {
        List<VoteDTO> votes = voteService.getVotesByTeamBoardMenuId(teamBoardMenuId);
        return ResponseEntity.ok(votes);
    }

    // 투표 결과 조회
    @GetMapping("/{teamBoardMenuId}/results")
    public ResponseEntity<Map<String, Long>> getVoteResultsByTeamBoardMenuId(@PathVariable Long teamBoardMenuId) {
        Map<String, Long> results = voteService.getVoteResultsByTeamBoardMenuId(teamBoardMenuId);
        return ResponseEntity.ok(results);
    }

    // 투표 삭제
    @DeleteMapping("/{voteId}")
    public ResponseEntity<Map<String, String>> deleteVote(@PathVariable Long voteId) {
        voteService.deleteVote(voteId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "투표가 성공적으로 삭제되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 팀 전체 투표 결과 조회
    @GetMapping("/team/{teamBoardId}/totalVotes")
    public ResponseEntity<Map<String, Long>> getTotalVotesByTeamBoardId(@PathVariable Long teamBoardId) {
        Map<String, Long> totalVotes = voteService.getTotalVotesByTeamBoardId(teamBoardId);
        return ResponseEntity.ok(totalVotes);
    }
}
