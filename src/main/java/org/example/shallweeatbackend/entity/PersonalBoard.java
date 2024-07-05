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
@Table(name = "personal_board")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class PersonalBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalBoardId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "personalBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalBoardMenu> personalBoardMenus;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate; // 생성 날짜

    @LastModifiedDate
    private LocalDateTime modifiedDate; // 수정 날짜

}
