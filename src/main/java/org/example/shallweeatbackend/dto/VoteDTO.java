package org.example.shallweeatbackend.dto;

import lombok.Data;

@Data
public class VoteDTO {
    private Long voteId;
    private Long teamBoardId;
    private Long teamBoardMenuId;
    private Long menuId;
    private Long userId;
}
