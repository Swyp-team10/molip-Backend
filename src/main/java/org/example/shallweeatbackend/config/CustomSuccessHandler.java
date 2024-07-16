package org.example.shallweeatbackend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.entity.RefreshToken;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.repository.UserRepository;
import org.example.shallweeatbackend.util.JWTUtil;
import org.example.shallweeatbackend.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * CustomSuccessHandler 클래스는 인증 성공 시 호출되는 핸들러입니다.
 * 인증 성공 후 JWT 토큰을 생성하고, 이를 쿠키에 저장하여 응답에 추가합니다.
 */
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil; // JWT 토큰 생성을 위한 유틸리티 클래스
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // 인증 성공 시 호출
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 인증된 사용자 정보 가져오기
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String providerId = customUserDetails.getProviderId(); // 사용자의 providerId 가져오기

        // 사용자의 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰 생성
        String refresh = jwtUtil.createJwt("refresh", providerId, role, 1209600000L); // 2주 (1209600000ms)

        // Refresh 토큰 저장
        addRefresh(providerId, refresh);

        // 응답 설정
        response.addCookie(createCookie(refresh));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("https://molip.site/home");
    }

    private void addRefresh(String providerId, String refresh) {
        // user entity 조회
        User user = userRepository.findByProviderId(providerId);

        // 동일한 user에 대한 기존 refresh token DB에서 삭제
        refreshTokenRepository.deleteByUser(user);

        // 새로운 refresh token DB에 저장
        LocalDateTime expirationTime = LocalDateTime.now().plusWeeks(2); // 현재 시간에 2주를 더하여 만료 시간 설정
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(refresh);
        refreshToken.setExpirationTime(expirationTime);

        refreshTokenRepository.save(refreshToken);
    }

    // 쿠키 생성
    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(14 * 24 * 60 * 60); // 쿠키의 유효 기간 설정 (14일, 초 단위)
        cookie.setDomain("molip.site");
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(true); // HTTPS 설정 시 필요
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setHttpOnly(true); // HTTP 전용 설정 (클라이언트 스크립트에서 접근 불가)

        return cookie; // 생성된 쿠키 반환
    }
}