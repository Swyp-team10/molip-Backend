package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendOptionsDTO {
    private List<String> tasteOptions;
    private List<String> carbOptions;
    private List<String> weatherOptions;
    private List<String> categoryOptions;
}
