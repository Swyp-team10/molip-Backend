package org.example.shallweeatbackend.controller;

import org.example.shallweeatbackend.dto.CountMembersNumDTO;
import org.example.shallweeatbackend.dto.CountVotedMembersNumDTO;
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
    public ResponseEntity<List<VoteDTO>> createVotes(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam Long teamBoardId, @RequestBody List<Long> menuIds) {
        List<VoteDTO> voteDTOs = voteService.createVotes(principal.getProviderId(), teamBoardId, menuIds);
        return new ResponseEntity<>(voteDTOs, HttpStatus.CREATED);
    }

    // 투표 수정
    @PatchMapping
    public ResponseEntity<List<VoteDTO>> updateVotes(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam Long teamBoardId, @RequestBody List<Long> menuIds) {
        List<VoteDTO> updatedVoteDTOs = voteService.updateVotes(principal.getProviderId(), teamBoardId, menuIds);
        return ResponseEntity.ok(updatedVoteDTOs);
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


    // 해당 게시판에 투표 참여한 인원 수 조회
    @GetMapping({"/{teamBoardId}"})
    public CountVotedMembersNumDTO getTeamBoardVotes(@PathVariable("teamBoardId") Long teamBoardId) {
        return voteService.getTeamBoardVotedMembers(teamBoardId);
    }

}
