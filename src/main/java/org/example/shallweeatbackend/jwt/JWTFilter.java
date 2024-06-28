package org.example.shallweeatbackend.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWTFilter 클래스는 HTTP 요청을 가로채서 JWT 토큰을 검증하고 사용자 인증을 처리하는 필터입니다.
 * OncePerRequestFilter를 상속하여 각 요청마다 한 번씩 실행됩니다.
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스

    // 요청을 필터링하여 JWT 토큰을 검증하고, 사용자 인증을 설정
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        // 쿠키가 null일 경우 빈 배열로 초기화
        if (cookies == null) {
            cookies = new Cookie[0];
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        // Authorization 헤더 검증 (Authorization 쿠키가 없을 경우)
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 값 설정
        String token = authorization;

        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 providerId와 role 획득
        String providerId = jwtUtil.getProviderId(token);
        String role = jwtUtil.getRole(token);

        // userDTO를 생성하여 값 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setProviderId(providerId);
        userDTO.setRole(role);

        // UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인에 요청 및 응답 객체 전달
        filterChain.doFilter(request, response);
    }
}
