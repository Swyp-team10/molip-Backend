package org.example.shallweeatbackend.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.jwt.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueTokenService {

    private final JWTUtil jwtUtil;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 리프레시 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            // 리프레시 토큰이 없을 경우 상태 코드 반환
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰이 만료되었을 경우 상태 코드 반환
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 리프레시 토큰인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            // 유효하지 않은 리프레시 토큰일 경우 상태 코드 반환
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String providerId = jwtUtil.getProviderId(refresh);
        String role = jwtUtil.getRole(refresh);

        // 새로운 액세스 토큰 생성
        String newAccess = jwtUtil.createJwt("access", providerId, role, 600000L);

        // 응답 헤더에 새로운 액세스 토큰 설정
        response.setHeader("access", newAccess);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
