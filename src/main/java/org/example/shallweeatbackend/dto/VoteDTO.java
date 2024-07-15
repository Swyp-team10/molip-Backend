package org.example.shallweeatbackend.dto;

import lombok.Data;

@Data
public class VoteDTO {
    private Long voteId;
    private Long teamBoardId;
    private Long userId;
    private String optionSelected;
}
