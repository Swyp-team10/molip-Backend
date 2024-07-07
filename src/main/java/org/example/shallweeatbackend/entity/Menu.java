package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    private String imageUrl;
    private String menuName;

    private String tasteOptions;
    private String carbOptions;
    private String weatherOptions;
    private String categoryOptions;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalBoardMenu> personalBoardMenus;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuTag> menuTags;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate; // 생성 날짜

    @LastModifiedDate
    private LocalDateTime modifiedDate; // 수정 날짜

}
