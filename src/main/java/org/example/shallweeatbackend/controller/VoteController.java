package org.example.shallweeatbackend.controller;

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

    // 특정 팀 보드의 모든 투표 및 메뉴의 투표 수 조회
    @GetMapping("/teamboards/{teamBoardId}/votes")
    public ResponseEntity<Map<String, Object>> getVoteResults(@PathVariable Long teamBoardId, @AuthenticationPrincipal CustomOAuth2User principal) {
        Map<String, Object> result = voteService.getVoteResults(teamBoardId, principal.getProviderId());
        return ResponseEntity.ok(result);
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
