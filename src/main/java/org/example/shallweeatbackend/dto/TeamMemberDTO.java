package org.example.shallweeatbackend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDTO {
    private Long teamMemberId;
    private Long teamBoardId;
    private String teamBoardName;
    private Long userId;
    private String userName;


}
