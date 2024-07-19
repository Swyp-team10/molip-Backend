package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    public ResponseEntity<VoteDTO> createVote(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam Long teamBoardId, @RequestParam Long menuId) {
        VoteDTO voteDTO = voteService.createVote(principal.getProviderId(), teamBoardId, menuId);
        return new ResponseEntity<>(voteDTO, HttpStatus.CREATED);
    }

    // 투표 수정
    @PatchMapping("/{voteId}")
    public ResponseEntity<VoteDTO> updateVote(@PathVariable Long voteId, @RequestParam Long teamBoardId, @RequestParam Long menuId) {
        VoteDTO updatedVoteDTO = voteService.updateVote(voteId, teamBoardId, menuId);
        return ResponseEntity.ok(updatedVoteDTO);
    }

    // 특정 팀 보드의 모든 투표 조회
    @GetMapping("/teamboard/{teamBoardId}/votes")
    public ResponseEntity<List<VoteDTO>> getVotesByTeamBoardId(@PathVariable Long teamBoardId) {
        List<VoteDTO> votes = voteService.getVotesByTeamBoardId(teamBoardId);
        return ResponseEntity.ok(votes);
    }

    // 특정 메뉴의 모든 투표 조회
    @GetMapping("/menu/{menuId}/votes")
    public ResponseEntity<List<VoteDTO>> getVotesByMenuId(@PathVariable Long menuId) {
        List<VoteDTO> votes = voteService.getVotesByMenuId(menuId);
        return ResponseEntity.ok(votes);
    }

    // 특정 메뉴에 대한 투표 수 조회
    @GetMapping("/menu/{menuId}/votes/count")
    public ResponseEntity<Map<String, Long>> countVotesByMenuId(@PathVariable Long menuId) {
        long count = voteService.countVotesByMenuId(menuId);
        Map<String, Long> response = new HashMap<>();
        response.put("voteCount", count);
        return ResponseEntity.ok(response);
    }

    // 투표 삭제
    @DeleteMapping("/{voteId}")
    public ResponseEntity<Map<String, String>> deleteVote(@PathVariable Long voteId) {
        voteService.deleteVote(voteId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "투표가 성공적으로 삭제되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
