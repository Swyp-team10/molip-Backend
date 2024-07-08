package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "teammenu")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TeamBoardMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamMenuId;

    @ManyToOne
    @JoinColumn(name = "teamboard_id", nullable = false)
    private PersonalBoard teamBoard;

    private String teammenu;

    private String category_options;


}
