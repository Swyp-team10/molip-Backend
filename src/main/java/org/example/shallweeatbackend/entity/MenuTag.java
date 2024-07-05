package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "menu_tag")
@Getter
@Setter
public class MenuTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuTagId;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
