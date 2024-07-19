package org.example.shallweeatbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddMenusToTeamBoardRequest {
    private List<Long> menuIds;
}
