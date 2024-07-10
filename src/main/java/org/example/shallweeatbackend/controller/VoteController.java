package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.VoteDTO;
import org.example.shallweeatbackend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public VoteDTO createVote(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam Long teamBoardId, @RequestParam String optionSelected) {
        // CustomOAuth2User에서 providerId를 가져와서 사용자 ID로 사용
        return voteService.createVote(principal.getProviderId(), teamBoardId, optionSelected);
    }

    @GetMapping("/{teamBoardId}")
    public ResponseEntity<List<VoteDTO>> getVotesByTeamBoardId(@PathVariable Long teamBoardId) {
        List<VoteDTO> votes = voteService.getVotesByTeamBoardId(teamBoardId);
        return ResponseEntity.ok(votes);
    }
}
