package org.example.shallweeatbackend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SearchWordResponse {

    private Long id;
    private String word;
    private LocalDateTime createdAt;
}
