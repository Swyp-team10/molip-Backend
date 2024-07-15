package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vote_id")
    private Long voteId;

    @ManyToOne
    @JoinColumn(name = "teamboard_id", nullable = false)
    private TeamBoard teamBoard;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "option_selected")
    private String optionSelected;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
}
