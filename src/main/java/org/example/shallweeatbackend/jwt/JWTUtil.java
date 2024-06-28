package org.example.shallweeatbackend.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWTUtil 클래스는 JWT(JSON Web Token)를 생성하고, 파싱하며, 검증하는 유틸리티 클래스입니다.
 * JWT를 사용하여 사용자 인증 정보를 안전하게 관리합니다.
 */
@Component
public class JWTUtil {

    private SecretKey secretKey; // 비밀 키 객체

    // 비밀 키 초기화
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT에서 providerId 추출
    public String getProviderId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("providerId", String.class);
    }

    // JWT에서 role 추출
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // JWT에서 category 추출 (ex: access, refresh)
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    // JWT 만료 여부 확인
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 새로운 JWT 생성
    public String createJwt(String category, String providerId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("providerId", providerId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();

    }
}
