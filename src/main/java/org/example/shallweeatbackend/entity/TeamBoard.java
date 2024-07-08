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
@Table(name="teamboard")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TeamBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "teamboard_id")
    private Long teamBoardId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="team_name")
    private String teamName;

    @Column(name="team_members_num")
    private Integer teamMembersNum;

    @Column(name="team_board_name")
    private String teamBoardName;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate; // 생성 날짜

    @LastModifiedDate
    private LocalDateTime modifiedDate; // 수정 날짜


//    @OneToMany(mappedBy = "teamboard", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TeamBoardMenu> teamBoardMenus;
}
