package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerId; // 식별자
    private String refreshToken; // 리프레시 토큰
    private LocalDateTime expirationTime; // 토큰 만료 일시

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate; // 토큰 생성일
}
