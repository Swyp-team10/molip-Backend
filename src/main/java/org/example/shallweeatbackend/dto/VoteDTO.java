package org.example.shallweeatbackend.dto;

import lombok.Data;

@Data
public class VoteDTO {
    private Long voteId;
    private Long teamBoardId;
    private Long teamBoardMenuId; // 추가: 투표 대상 메뉴 ID
    private Long userId;
    private String optionSelected;
}
