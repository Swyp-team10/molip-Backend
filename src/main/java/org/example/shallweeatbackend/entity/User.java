package org.example.shallweeatbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 이름(별명)
    private String email; // 이메일
    private String providerId; // 제공자 이름과 ID를 조합한 고유 식별자 (ex: kakao_1531413412)
    private String role; // 권한
}
