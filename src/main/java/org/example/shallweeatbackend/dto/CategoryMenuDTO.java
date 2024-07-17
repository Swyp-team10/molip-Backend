package org.example.shallweeatbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryMenuDTO {

    private String category;
    private List<MenuDTO> menu;

    @Data
    public static class MenuDTO {
        private Long menuId;
        private String imageUrl;
        private String menuName;
        private List<String> tags;
    }
}
