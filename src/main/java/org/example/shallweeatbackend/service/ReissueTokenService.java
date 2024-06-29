package org.example.shallweeatbackend.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.entity.RefreshToken;
import org.example.shallweeatbackend.jwt.JWTUtil;
import org.example.shallweeatbackend.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReissueTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 리프레시 토큰 가져오기
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            // 리프레시 토큰이 없을 경우 상태 코드 반환
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 만료 여부 확인
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰이 만료되었을 경우 상태 코드 반환
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // refresh token이 DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {
            // DB에 저장되어 있지 않은 경우 상태 코드 반환
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 리프레시 토큰인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {
            // 유효하지 않은 리프레시 토큰일 경우 상태 코드 반환
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String providerId = jwtUtil.getProviderId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtUtil.createJwt("access", providerId, role, 1800000L); // 30분 (1800000ms)
        String newRefreshToken = jwtUtil.createJwt("refresh", providerId, role, 1209600000L); // 2주 (1209600000ms)

        // DB에 기존의 refresh token 삭제 후 새 refresh token 저장
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        addRefresh(providerId, newRefreshToken);

        // 응답 헤더에 새로운 액세스 토큰 설정
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie(newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

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
