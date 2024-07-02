package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name; // 이름(별명)
    private String email; // 이메일
    private String providerId; // 제공자 이름과 ID를 조합한 고유 식별자 (ex: kakao_1531413412)
    private String role; // 권한

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate; // 가입 날짜

    @LastModifiedDate
    private LocalDateTime modifiedDate; // 수정 날짜

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefreshToken refreshToken; // 사용자의 리프레시 토큰
}