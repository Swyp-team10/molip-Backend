package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="team_member")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TeamMember {
    @Id
    @Column(name = "team_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamMemberId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "teamboard_id", nullable = false)
    private TeamBoard teamBoard;

}
