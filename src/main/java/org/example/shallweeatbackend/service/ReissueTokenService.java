package org.example.shallweeatbackend.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.entity.RefreshToken;
import org.example.shallweeatbackend.util.JWTUtil;
import org.example.shallweeatbackend.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 리프레시 토큰을 재발급하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ReissueTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 클라이언트의 HttpServletRequest에서 refresh 토큰 추출
        String refreshToken = getRefreshTokenFromRequest(request);

        // 추출된 refresh 토큰이 없는 경우, 클라이언트에게 400 응답 반환
        if (refreshToken == null) {
            return new ResponseEntity<>("Refresh token is required.", HttpStatus.BAD_REQUEST);
        }

        try {
            // refresh 토큰 만료 여부 검증
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            // refresh 토큰이 만료된 경우, 클라이언트에게 401 응답 반환
            return new ResponseEntity<>("Refresh token expired.", HttpStatus.UNAUTHORIZED);
        }

        // DB에서 refresh 토큰의 존재 여부 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {
            // 존재하지 않는 경우, 클라이언트에게 400 응답 반환
            return new ResponseEntity<>("Refresh token not found.", HttpStatus.BAD_REQUEST);
        }

        // refresh 토큰 타입 검사
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            // 유효하지 않은 경우, 클라이언트에게 400 응답 반환
            return new ResponseEntity<>("Invalid token type.", HttpStatus.BAD_REQUEST);
        }

        // 새로운 access 토큰과 refresh 토큰을 발급하고, DB에 새 refresh 토큰 추가
        String providerId = jwtUtil.getProviderId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String newAccessToken = jwtUtil.createJwt("access", providerId, role, 1800000L); // 30분 (1800000ms)
        String newRefreshToken = jwtUtil.createJwt("refresh", providerId, role, 1209600000L); // 2주 (1209600000ms)
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        addRefresh(providerId, newRefreshToken);

        // 새로 발급된 access 토큰 응답 헤더에 설정하고, 새로운 refresh 토큰 쿠키로 전송
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie(newRefreshToken));

        // 클라이언트에게 200 OK 응답 반환
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // refresh 토큰 추출
    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // refresh 토큰 DB에 저장
    private void addRefresh(String providerId, String refreshToken) {
        // 현재 시간에 2주를 더하여 만료 시간 설정
        LocalDateTime expirationTime = LocalDateTime.now().plusWeeks(2);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setProviderId(providerId);
        refreshEntity.setRefreshToken(refreshToken);
        refreshEntity.setExpirationTime(expirationTime);

        refreshTokenRepository.save(refreshEntity);
    }

    // 쿠키 생성
    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(14 * 24 * 60 * 60); // 쿠키의 유효 기간 설정 (14일, 초 단위)
        //cookie.setSecure(true); // HTTPS 설정 시 필요
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setHttpOnly(true); // HTTP 전용 설정 (클라이언트 스크립트에서 접근 불가)

        return cookie; // 생성된 쿠키 반환
    }
}
