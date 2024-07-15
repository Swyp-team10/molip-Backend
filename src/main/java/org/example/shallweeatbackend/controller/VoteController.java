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

@RestController
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteDTO> createVote(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam Long teamBoardId, @RequestParam String optionSelected) {
        VoteDTO voteDTO = voteService.createVote(principal.getProviderId(), teamBoardId, optionSelected);
        return new ResponseEntity<>(voteDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{voteId}")
    public ResponseEntity<VoteDTO> updateVote(@PathVariable Long voteId, @RequestParam String optionSelected) {
        VoteDTO voteDTO = voteService.updateVote(voteId, optionSelected);
        return ResponseEntity.ok(voteDTO);
    }

    @GetMapping("/{teamBoardId}")
    public ResponseEntity<List<VoteDTO>> getVotesByTeamBoardId(@PathVariable Long teamBoardId) {
        List<VoteDTO> votes = voteService.getVotesByTeamBoardId(teamBoardId);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/{teamBoardId}/results")
    public ResponseEntity<Map<String, Long>> getVoteResultsByTeamBoardId(@PathVariable Long teamBoardId) {
        Map<String, Long> results = voteService.getVoteResultsByTeamBoardId(teamBoardId);
        return ResponseEntity.ok(results);
    }
}
