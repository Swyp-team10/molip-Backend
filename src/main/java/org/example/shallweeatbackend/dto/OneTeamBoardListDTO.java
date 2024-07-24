package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OneTeamBoardListDTO {
    private Long teamBoardId;
    private String teamBoardName;
    private String teamName;
    private Integer teamMembersNum;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean hasUserAddedMenu; // 사용자가 메뉴를 추가했는지 여부
}
