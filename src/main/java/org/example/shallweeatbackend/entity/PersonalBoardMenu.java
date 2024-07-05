package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "personal_board_menu")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class PersonalBoardMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalBoardMenuId;

    @ManyToOne
    @JoinColumn(name = "personal_board_id", nullable = false)
    private PersonalBoard personalBoard;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;


}
