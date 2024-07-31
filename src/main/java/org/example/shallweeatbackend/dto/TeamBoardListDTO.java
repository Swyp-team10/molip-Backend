package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamBoardListDTO {
    private Long teamBoardId;
    private String teamBoardName;
    private String teamName;
    private Integer teamMembersNum;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean hasUserAddedMenu; // 사용자가 메뉴를 추가했는지 여부
    private boolean isVoted; // 투표 여부
    private boolean isAllPeopleAdded; // 팀 메뉴판에 모든 사용자가 메뉴 투표에 참여 했는지 여부

}
