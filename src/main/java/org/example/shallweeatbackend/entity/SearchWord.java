package org.example.shallweeatbackend.entity;

import java.time.LocalDateTime;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "search_words")
public class SearchWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchWordId;

    private String word;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
