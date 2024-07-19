package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamBoardMenuDTO {
    private Long teamBoardMenuId;
    private String teamBoardName;
    private Long menuId;
    private String imageUrl;
    private String menuName;
    private String categoryOptions;
    private List<String> tags;

    public String getTeamBoardName() {
        return teamBoardName;
    }

    public void setTeamBoardName(String teamBoardName) {
        this.teamBoardName = teamBoardName;
    }

}
