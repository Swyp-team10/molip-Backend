package org.example.shallweeatbackend.dto;

import lombok.Data;

@Data
public class TeamBoardRequest {
    private String teamBoardName;
    private String teamName;
    private Integer teamMembersNum;
}
