package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonalBoardDTO {
    private Long personalBoardId;
    private String name;
    private String userName;
    private String userEmail;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
