package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamBoardDTO { // 팀 메뉴판 생성 시 이용할 객체

    private Long teamBoardId;
    private String teamName;
    private Integer teamMembersNum;
    private String teamBoardName;
    private String userName;
    private String userEmail;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
