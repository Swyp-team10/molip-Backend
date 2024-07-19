package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoteDTO {
    private Long voteId;
    private Long teamBoardId;
    private Long menuId;
    private Long userId;
    private Long teamBoardMenuId;
    private LocalDateTime createdDate;
    private String menuName;
}
