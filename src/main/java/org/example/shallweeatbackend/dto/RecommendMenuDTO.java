package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendMenuDTO {
    private Long menuId;
    private String imageUrl;
    private String menuName;
    private String categoryOptions;
    private List<String> tags;
}
