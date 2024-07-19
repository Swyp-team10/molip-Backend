package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@Table(name = "teamboardmenu")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TeamBoardMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teamboardmenu_id")
    private Long teamBoardMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamboard_id", nullable = false)
    private TeamBoard teamBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;




}
