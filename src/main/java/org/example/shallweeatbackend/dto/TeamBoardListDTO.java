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
}
