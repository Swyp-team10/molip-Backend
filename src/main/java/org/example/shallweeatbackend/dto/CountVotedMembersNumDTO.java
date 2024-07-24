package org.example.shallweeatbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountVotedMembersNumDTO {
    private Long votedUserCount;
    private Integer teamMembersNum;
}
